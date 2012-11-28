/*
 * Copyright 2012 CoreMedia AG
 *
 * This file is part of Joala.
 *
 * Joala is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Joala is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.junit;

import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.Parameterized;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.ClassUtils.isAssignable;

/**
 * <p>
 * Default builder for parameters. Each added set of parameters is verified to be of the same length.
 * In addition the provided test class is verified if it meets the specified requirements, that is
 * has a constructor matching the provided arguments and has the correct runner configured.
 * </p>
 *
 * @since 10/8/12
 */
public class DefaultParameterizedParametersBuilder implements ParameterizedParametersBuilder {
  private static final int EXPECTED_PARAMETER_SETS_COUNT = 10;
  private final List<Object[]> parameterSets = new ArrayList<Object[]>(EXPECTED_PARAMETER_SETS_COUNT);
  private final Class<?> testClass;
  private final String testName;
  private Class<?>[] parameterTypes;

  public DefaultParameterizedParametersBuilder(final Class<?> testClass) {
    this.testClass = testClass;
    testName = this.testClass.getName();
  }

  @Override
  public ParameterizedParametersBuilder add(final Object... objects) {
    if (parameterTypes == null) {
      validateClass();
    }
    final int parameterSetPosition = parameterSets.size() + 1;
    checkArgument(
            objects.length == parameterTypes.length,
            format("Parameter length mismatch for set %d (expected %d, was %d)",
                    parameterSetPosition,
                    parameterTypes.length,
                    objects.length));
    for (int i = 0; i < objects.length; i++) {
      final Object object = objects[i];
      if (object != null) {
        final Class<?> expectedType = parameterTypes[i];
        checkArgument(isAssignable(object.getClass(), expectedType, true),
                format("Parameter type mismatch for set %d at position %d (expected %s, was %s)",
                        parameterSetPosition,
                        i + 1,
                        expectedType.getName(),
                        object.getClass().getName()
                ));
      }
    }
    parameterSets.add(objects);
    return this;
  }

  private void validateClass() {
    final RunWith runWith = testClass.getAnnotation(RunWith.class);
    if (runWith == null) {
      throw new ParameterizedTestMismatchException(format("Test %s has no @RunWith annotation.", testName));
    }
    final Class<? extends Runner> runner = runWith.value();
    if (!Parameterized.class.isAssignableFrom(runner)) {
      throw new ParameterizedTestMismatchException(
              format("Test %s is not marked with expected runner %s.", testName, Parameterized.class.getName()));
    }
    final Constructor<?>[] constructors = testClass.getConstructors();
    if (constructors.length == 0) {
      throw new ParameterizedTestMismatchException(format("%s misses constructor to receive parameter values.", testName));
    }
    if (constructors.length > 1) {
      throw new ParameterizedTestMismatchException(format("%s must not have more than one constructor", testName));
    }
    final Constructor<?> constructor = constructors[0];
    parameterTypes = constructor.getParameterTypes();
  }

  @Override
  public Collection<Object[]> build() {
    return Collections.unmodifiableList(parameterSets);
  }

}

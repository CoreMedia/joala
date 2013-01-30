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

package net.joala.condition;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.joala.bdd.reference.Reference;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.google.common.collect.Iterators.forArray;
import static com.google.common.collect.Iterators.tryFind;
import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.typeCompatibleWith;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assume.assumeThat;
import static org.mockito.Mockito.mock;

/**
 * @since 0.8.0
 */
public class DefaultConditionCommonSteps {
  private Class<? extends DefaultCondition> conditionClass;

  public Class<? extends DefaultCondition> getConditionClass() {
    return conditionClass;
  }

  public void setConditionClass(final Class<? extends DefaultCondition> conditionClass) {
    this.conditionClass = conditionClass;
  }

  public static Iterable<Object> getParamValues(final Constructor<?> constructor, final GivenParameterValue<?>... givenExpressions) {
    final List<Object> parameters = newArrayList();
    final Class<?>[] parameterTypes = constructor.getParameterTypes();
    final Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
    for (int i = 0; i < parameterTypes.length; i++) {
      final Class<?> parameterType = parameterTypes[i];
      final Annotation[] parameterAnnotation = parameterAnnotations[i];
      final Object paramValue = getParamValue(parameterType, parameterAnnotation, givenExpressions);
      parameters.add(paramValue);
    }
    return parameters;
  }

  public static Object getParamValue(final Class<?> parameterType, final Annotation[] parameterAnnotations, final GivenParameterValue<?>... givenParameterValues) {
    final Object paramValue;
    final Optional<GivenParameterValue<?>> optional =
            tryFind(forArray(givenParameterValues),
                    new MatchesParameterType(parameterType));
    if (optional.isPresent()) {
      paramValue = optional.get().getParameterValue();
    } else {
      paramValue = getParamValue(parameterType, parameterAnnotations);
    }
    return paramValue;
  }

  public static Object getParamValue(final Class<?> parameterType, final Annotation... parameterAnnotations) {
    final Object paramValue;
    if (hasItemInArray(instanceOf(Nullable.class)).matches(parameterAnnotations)) {
      paramValue = null;
    } else {
      paramValue = mock(parameterType);
    }
    return paramValue;
  }

  public static Throwable tryConstruct(final Constructor<?> constructor, final Iterable<Object> parameters) {
    try {
      construct(constructor, parameters);
      return null;
    } catch (InvocationTargetException e) {
      return e.getCause();
    } catch (Throwable e) {
      return e;
    }
  }

  public static Object construct(final Constructor<?> constructor, final Iterable<Object> parameters)
          throws InstantiationException, IllegalAccessException, InvocationTargetException {
    return constructor.newInstance(Iterables.toArray(parameters, Object.class));
  }

  public void given_constructors_C_for_conditions_with_argument_of_type_$1(
          final Reference<Iterable<? extends Constructor<?>>> constructorsReference,
          final Class<?> parameterType) {
    final Class<? extends DefaultCondition> conditionClass1 = getConditionClass();
    given_constructors_$0_for_type_$1_with_argument_of_type_$2(constructorsReference, conditionClass1, parameterType);
  }

  public void given_constructors_$0_for_type_$1_with_argument_of_type_$2(final Reference<Iterable<? extends Constructor<?>>> constructorsReference, final Class<? extends DefaultCondition> conditionClass1, final Class<?> parameterType) {
    final Constructor<?>[] allConstructors = conditionClass1.getConstructors();
    final Matcher<Class<?>> parameterTypeMatcher = typeCompatibleWith(parameterType);
    final Iterable<Constructor<?>> matched = Iterables.filter(newArrayList(allConstructors), new HasParameter(parameterTypeMatcher));
    constructorsReference.set(matched);
    assumeThat("Some constructors should have been found.", matched, Matchers.not(Matchers.emptyIterable()));
  }

  public static class HasParameter implements Predicate<Constructor<?>> {
    private final Matcher<Class<?>> parameterTypeMatcher;

    public HasParameter(final Matcher<Class<?>> parameterTypeMatcher) {
      this.parameterTypeMatcher = parameterTypeMatcher;
    }

    @Override
    public boolean apply(@Nullable final Constructor<?> input) {
      assert input != null;
      final Matcher<Class<?>[]> matcher = hasItemInArray(parameterTypeMatcher);
      final Class<?>[] parameterTypes = input.getParameterTypes();
      return matcher.matches(parameterTypes);
    }
  }

  public static final class GivenParameterValue<T> {
    private final Class<T> parameterClass;
    private final T parameterValue;

    public GivenParameterValue(final Class<T> parameterClass, final T parameterValue) {
      this.parameterClass = parameterClass;
      this.parameterValue = parameterValue;
    }

    public Class<T> getParameterClass() {
      return parameterClass;
    }

    public T getParameterValue() {
      return parameterValue;
    }

    public static <T> GivenParameterValue<T> givenParameter(final Class<T> clazz) {
      return new GivenParameterValue<T>(clazz, mock(clazz));
    }

    public static <T> GivenParameterValue<T> givenParameter(final Class<T> clazz, final T value) {
      return new GivenParameterValue<T>(clazz, value);
    }

  }

  public static class MatchesParameterType implements Predicate<GivenParameterValue<?>> {
    private final Class<?> parameterType;

    public MatchesParameterType(final Class<?> parameterType) {
      this.parameterType = parameterType;
    }

    @Override
    public boolean apply(@Nullable final GivenParameterValue<?> input) {
      assert input != null;
      return input.getParameterClass().isAssignableFrom(parameterType);
    }
  }

}

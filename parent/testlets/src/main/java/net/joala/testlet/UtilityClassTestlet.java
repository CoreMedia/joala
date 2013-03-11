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
 * along with Joala.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.testlet;

import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isFinal;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;

/**
 * <p>
 * Test to verify if provided class meets utility class requirements.
 * This is especially useful to get code coverage for required private
 * constructors.
 * </p>
 *
 * @since 10/5/12
 */
@SuppressWarnings("JUnitTestClassNamingConvention")
@Ignore("Don't execute Testlets in IDE and alike.")
public class UtilityClassTestlet extends AbstractTestlet<Class<?>> {


  /**
   * <p>
   * Builds the testlet with the specified class under test.
   * </p>
   *
   * @param testling artifact under test
   */
  protected UtilityClassTestlet(@Nonnull final Class<?> testling) {
    super(testling);
  }

  @Test
  public void utilityClass_should_have_private_noarg_constructor() {
    try {
      final Constructor<?> declaredConstructor = getTestling().getDeclaredConstructor();
      final int modifiers = declaredConstructor.getModifiers();
      assertTrue("Noarg constructor must be private.", Modifier.isPrivate(modifiers));
    } catch (NoSuchMethodException ignored) {
      fail(format("Class %s has no noarg constructor.", getTestling()));
    }
  }

  @Test
  public void utilityClass_should_be_final() {
    assertTrue("Utility Class should be final.", isFinal(getTestling().getModifiers()));
  }

  @Test
  public void utilityClass_noarg_constructor_should_pass() throws InvocationTargetException, IllegalAccessException, InstantiationException {
    try {
      final Constructor<?> declaredConstructor = getTestling().getDeclaredConstructor();
      declaredConstructor.setAccessible(true);
      try {
        declaredConstructor.newInstance();
      } catch (Exception e) {
        assertThat("Should have called noarg constructor without failure.", e, nullValue());
      }
    } catch (NoSuchMethodException e) {
      assumeThat(e, nullValue());
    }
  }

  @SuppressWarnings("ProhibitedExceptionDeclared")
  public static UtilityClassTestlet utilityClassTestlet(@Nonnull final Class<?> utilityClassUnderTest) throws Throwable { // NOSONAR: Throwable inherited from JUnit
    return new UtilityClassTestlet(utilityClassUnderTest);
  }

}

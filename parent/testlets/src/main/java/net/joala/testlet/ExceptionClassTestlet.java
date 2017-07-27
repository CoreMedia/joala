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

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;

import static net.joala.matcher.reflect.ClassHasModifier.classIsPublic;
import static net.joala.matcher.reflect.MemberHasModifier.memberIsPublic;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.typeCompatibleWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Test an exception and its constructors. The testlet is prepared to
 * deal with standard exception constructors. It will scan for them,
 * feed them with information and see if the information will be
 * available after construction.
 * </p>
 *
 * @since 10/5/12
 * @deprecated Will be removed in one of the next releases.
 */
@SuppressWarnings({"JUnitTestClassNamingConvention", "deprecation"})
@Ignore("Don't execute Testlets in IDE and alike.")
@Deprecated
public class ExceptionClassTestlet<T extends Throwable> extends AbstractTestlet<Class<T>> {
  private static final int RANDOM_MESSAGE_LENGTH = 64;

  /**
   * <p>
   * Builds the testlet with the specified class under test.
   * </p>
   *
   * @param testling artifact under test
   */
  protected ExceptionClassTestlet(@Nonnull final Class<T> testling) {
    super(testling);
  }

  @Test
  public void exception_should_be_public() {
    assertThat(getTestling(), classIsPublic());
  }

  @Test
  public void constructor_noarg_should_meet_requirements() throws NoSuchMethodException {
    validateConstructor();
  }

  @Test
  public void constructor_string_should_meet_requirements() throws NoSuchMethodException {
    validateConstructor(String.class);
  }

  @Test
  public void constructor_throwable_should_meet_requirements() throws NoSuchMethodException {
    validateConstructor(Throwable.class);
  }

  @Test
  public void constructor_string_throwable_should_meet_requirements() throws NoSuchMethodException {
    validateConstructor(String.class, Throwable.class);
  }

  @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
  private void validateConstructor(final Class<?>... parameterTypes) {
    final Constructor<T> constructor;
    try {
      constructor = getTestling().getDeclaredConstructor(parameterTypes);
    } catch (NoSuchMethodException ignored) {
      return; // ignore... the constructor just does not seem to exist
    }
    assertThat(constructor, memberIsPublic());
    String message = null;
    Throwable cause = null;
    final Object[] initArgs = new Object[parameterTypes.length];
    if (parameterTypes.length > 0) {
      if (String.class.equals(parameterTypes[0])) {
        message = RandomStringUtils.random(RANDOM_MESSAGE_LENGTH);
        initArgs[0] = message;
      } else {
        assertThat("Single arg constructor: Must be either String or Throwable.", parameterTypes[0], typeCompatibleWith(Throwable.class));
        cause = new Exception(RandomStringUtils.random(RANDOM_MESSAGE_LENGTH));
        initArgs[0] = cause;
      }
      if (parameterTypes.length > 1) {
        assertThat("Two arg constructor: First arg must be String.", parameterTypes[0], typeCompatibleWith(String.class));
        assertThat("Two arg constructor: Second arg must be Throwable.", parameterTypes[1], typeCompatibleWith(Throwable.class));
        cause = new Exception(RandomStringUtils.random(RANDOM_MESSAGE_LENGTH));
        initArgs[1] = cause;
      }
    }
    try {
      final T thr = constructor.newInstance(initArgs);
      if (message != null) {
        assertEquals("Message should have been set correctly.", message, thr.getMessage());
      }
      if (cause != null) {
        assertSame("Cause should have been set correctly.", cause, thr.getCause());
      }
    } catch (Exception e) {
      assertThat("Constructor must have passed without exception.", e, nullValue());
    }
  }

  @SuppressWarnings("ProhibitedExceptionDeclared")
  public static <T extends Throwable> ExceptionClassTestlet<T> exceptionClassTestlet(@Nonnull final Class<T> exceptionClassUnderTest) throws Throwable { // NOSONAR: Throwable inherited from JUnit
    return new ExceptionClassTestlet<T>(exceptionClassUnderTest);
  }

}

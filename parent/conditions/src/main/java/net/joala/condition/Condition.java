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

package net.joala.condition;

import net.joala.condition.timing.WaitTimeoutException;
import net.joala.expression.Expression;
import net.joala.expression.ExpressionEvaluationException;
import org.hamcrest.Matcher;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A test condition to wait for using a given timeout. If the condition does not become true within
 * time exceptions will be thrown.
 *
 * @param <T> the type of the value which will be verified
 * @since 2/24/12
 */
public interface Condition<T> {
  /**
   * Conditions need to implement this method to return the value
   * which will be checked.
   *
   * @return the value
   * @throws ExpressionEvaluationException when the actual value cannot be evaluated. Consider calling any of the
   *                                       {@code await} or {@code assume} methods to wait for the value to
   *                                       become available.
   * @see Expression#get()
   */
  T get();

  /**
   * <p>
   * Retrieve the result of the condition as soon as it is evaluated without any exception.
   * </p>
   *
   * @return the retrieved value
   * @throws WaitTimeoutException if a value could not be retrieved in time
   */
  T await();

  /**
   * <p>
   * Wait for the condition value until it fulfills the given matcher and return it.
   * </p>
   *
   * @param matcher matcher to use
   * @return the value which fulfills the given matcher
   * @throws WaitTimeoutException if a value could not be retrieved in time
   */
  T await(@Nonnull Matcher<? super T> matcher);

  /**
   * <p>
   * Assumes that the condition evaluates to the expected value within a given time.
   * </p>
   *
   * @param expected expected value
   */
  void assumeEquals(@Nullable T expected);

  /**
   * Uses {@link Matcher} to assume that a condition is met in time.
   *
   * @param matcher the matcher to use
   */
  void assumeThat(@Nonnull Matcher<? super T> matcher);

  /**
   * <p>
   * Assert that the condition evaluates to the expected value within a given time.
   * </p>
   *
   * @param expected expected value
   */
  void assertEquals(@Nullable T expected);

  /**
   * Uses {@link Matcher} to assert that a condition is met in time.
   *
   * @param matcher the matcher to use
   */
  void assertThat(@Nonnull Matcher<? super T> matcher);

  /**
   * <p>
   * Wait until the condition evaluates to the expected value within a given time.
   * </p>
   *
   * @param expected expected value
   */
  void waitUntilEquals(@Nullable T expected);

  /**
   * Uses {@link Matcher} to wait until a condition is met in time.
   *
   * @param matcher the matcher to use
   */
  void waitUntil(@Nonnull Matcher<? super T> matcher);

  /**
   * Sets a factor to speed up or slow down tests. If &lt; 1.0 the tests will speed up at least
   * for failures while a number &gt; 1.0 increases the time to wait for the condition by
   * the given factor.
   *
   * @param factor factor by which to increase the timeout for this condition
   * @return a self-reference.
   */
  @Nonnull
  Condition<T> withTimeoutFactor(@Nonnegative double factor);

  /**
   * The message to print on failure.
   *
   * @param message message with debugging hints; {@code null} to remove the message
   * @return self-reference
   */
  @Nonnull
  Condition<T> withMessage(@Nullable String message);
}

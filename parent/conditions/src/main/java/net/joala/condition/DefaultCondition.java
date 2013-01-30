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

import com.google.common.base.Objects;
import net.joala.condition.timing.DeceleratingWaitFactory;
import net.joala.condition.timing.Wait;
import net.joala.condition.timing.WaitFactory;
import net.joala.condition.timing.WaitFailStrategy;
import net.joala.condition.timing.WaitTimeoutFailStrategy;
import net.joala.expression.Expression;
import net.joala.time.Timeout;
import org.hamcrest.Matcher;
import org.hamcrest.core.CombinableMatcher;
import org.hamcrest.core.IsAnything;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * <p>
 * An abstract implementation for conditions. Using this you only have to override the method {@link #get()} to retrieve
 * the value/state to check directly. Everything else, waiting, asserting etc. is done by this condition implementation.
 * </p>
 * <p>
 * To override this class and provide the same syntax for configuration you should override any methods returning
 * a self reference, call the super method and just return {@code this}.
 * </p>
 *
 * @param <T> result type of {@link #get()}
 * @since 2/24/12
 */
public class DefaultCondition<T> implements Condition<T>, FailSafeCondition<T> {
  private static final WaitFailStrategy TIMEOUT_FAIL_STRATEGY = new WaitTimeoutFailStrategy();
  private static final WaitFailStrategy ASSUMPTION_FAIL_STRATEGY = new WaitAssumptionFailStrategy();
  private static final WaitFailStrategy ASSERTION_FAIL_STRATEGY = new WaitAssertionFailStrategy();
  /**
   * Message to print on failure. {@code null} for none.
   */
  @Nullable
  private String message;

  /**
   * Runnable to execute after condition has been performed.
   */
  @Nullable
  private Runnable runFinallyRunnable;

  /**
   * Runnable to execute before the condition is being performed.
   */
  @Nullable
  private Runnable runBeforeRunnable;

  /**
   * The expression to retrieve the value/state while waiting.
   */
  @Nonnull
  private final Expression<T> expression;
  @Nonnegative
  private double factor = 1.0;
  @Nonnull
  private final WaitFactory waitFactory;

  /**
   * <p>
   * Constructs a condition which evaluates the given expression and applies the given timeout.
   * Uses a default strategy for waiting.
   * </p>
   *
   * @param expression expression to evaluate until timeout or until it matches
   * @param timeout timeout when expression evaluation will end
   */
  public DefaultCondition(@Nonnull final Expression<T> expression, @Nonnull final Timeout timeout) {
    this(expression, new DeceleratingWaitFactory(timeout));
  }

  /**
   * <p>
   * Constructs a condition which evaluates the given expression with a wait strategy
   * supplied by the given factory.
   * </p>
   *
   * @param expression expression to evaluate until timeout or until it matches
   * @param waitFactory factory to create wait strategies
   * @since 0.8.0
   */
  public DefaultCondition(@Nonnull final Expression<T> expression, @Nonnull final WaitFactory waitFactory) {
    checkNotNull(expression, "Expression must not be null.");
    checkNotNull(waitFactory, "WaitFactory must not be null.");
    this.expression = expression;
    this.waitFactory = waitFactory;
  }

  @Override
  @Nullable
  public final T get() {
    return expression.get();
  }

  @Override
  @Nullable
  public final T await() {
    return await(IsAnything.anything());
  }

  @Override
  @Nullable
  public T await(@Nonnull final Matcher<? super T> matcher) {
    return until(matcher, TIMEOUT_FAIL_STRATEGY);
  }

  @Nonnull
  @Override
  public T awaitNonNull() {
    return until(notNullValue(), TIMEOUT_FAIL_STRATEGY);
  }

  @Nonnull
  @Override
  public T awaitNonNull(@Nonnull final Matcher<? super T> matcher) {
    return until(CombinableMatcher.<T>both(matcher).and(notNullValue()), TIMEOUT_FAIL_STRATEGY);
  }

  private T until(final Matcher<? super T> matcher, final WaitFailStrategy failStrategy) {
    return until(waitFactory.get(factor, failStrategy), matcher);
  }

  private T until(@Nonnull final Wait wait, @Nullable final Matcher<? super T> matcher) {
    if (runBeforeRunnable != null) {
      runBeforeRunnable.run();
    }
    try {
      return wait.until(message, expression, new ExpressionFunction<T>(), matcher);
    } finally {
      if (runFinallyRunnable != null) {
        runFinallyRunnable.run();
      }
    }
  }

  @Override
  public void assumeThat(@Nonnull final Matcher<? super T> matcher) {
    until(matcher, ASSUMPTION_FAIL_STRATEGY);
  }

  @Override
  public final void assumeEquals(@Nullable final T expected) {
    assumeThat(equalTo(expected));
  }

  @Override
  public void assertThat(@Nonnull final Matcher<? super T> matcher) {
    until(matcher, ASSERTION_FAIL_STRATEGY);
  }

  @Override
  public final void assertEquals(@Nullable final T expected) {
    assertThat(equalTo(expected));
  }

  @Override
  @Nonnull
  public DefaultCondition<T> runFinally(@Nullable final Runnable runnable) {
    runFinallyRunnable = runnable;
    return this;
  }

  @Override
  @Nonnull
  public DefaultCondition<T> runBefore(@Nullable final Runnable runnable) {
    runBeforeRunnable = runnable;
    return this;
  }

  @Override
  @Nonnull
  public DefaultCondition<T> withTimeoutFactor(@Nonnegative final double newFactor) {
    this.factor = newFactor;
    return this;
  }

  @Override
  @Nonnull
  public DefaultCondition<T> withMessage(@Nullable final String newMessage) {
    this.message = newMessage;
    return this;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("message", message)
            .add("expression", expression)
            .add("runBefore", runBeforeRunnable)
            .add("runFinally", runFinallyRunnable)
            .add("waitFactory", waitFactory)
            .add("factor", factor)
            .toString();
  }


}

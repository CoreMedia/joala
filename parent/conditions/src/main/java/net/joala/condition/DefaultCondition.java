/*
 * Copyright 2012 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.joala.condition;

import com.google.common.base.Objects;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.core.IsEqual.equalTo;

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
  private static final ConditionWaitTimeoutFailStrategy TIMEOUT_FAIL_STRATEGY =
          new ConditionWaitTimeoutFailStrategy();
  private static final ConditionWaitAssumptionFailStrategy ASSUMPTION_FAIL_STRATEGY =
          new ConditionWaitAssumptionFailStrategy();
  private static final ConditionWaitAssertionFailStrategy ASSERTION_FAIL_STRATEGY =
          new ConditionWaitAssertionFailStrategy();
  /**
   * Message to print on failure. {@code null} for none.
   */
  @Nullable
  private String message;

  /**
   * Runnable to execute after condition has been performed.
   */
  @Nullable
  private Runnable runFinally;

  /**
   * Runnable to execute before the condition is being performed.
   */
  @Nullable
  private Runnable runBefore;

  @Nonnull
  private final Timeout timeout;

  /**
   * The expression to retrieve the value/state while waiting.
   */
  @Nonnull
  private final Expression<T> expression;
  @Nonnegative
  private double factor = 1.0;

  public DefaultCondition(@Nonnull final Expression<T> expression, @Nonnull final Timeout timeout) {
    checkNotNull(expression, "Expression must not be null.");
    checkNotNull(timeout, "Timeout must not be null.");
    this.expression = expression;
    this.timeout = timeout;
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
    final ConditionFunction<T> function = new ConditionFunction<T>(expression);
    until(new ConditionWaitImpl<T>(
            message,
            function,
            matcher,
            timeout.in(TimeUnit.MILLISECONDS, factor),
            TIMEOUT_FAIL_STRATEGY));
    return function.getCached();
  }

  private void until(@Nonnull final ConditionWait wait) {
    if (runBefore != null) {
      runBefore.run();
    }
    try {
      wait.until();
    } finally {
      if (runFinally != null) {
        runFinally.run();
      }
    }
  }

  @Override
  public void assumeThat(@Nonnull final Matcher<? super T> matcher) {
    until(new ConditionWaitImpl<T>(
            message,
            new ConditionFunction<T>(expression),
            matcher,
            timeout.in(TimeUnit.MILLISECONDS, factor),
            ASSUMPTION_FAIL_STRATEGY));
  }

  @Override
  public final void assumeEquals(@Nullable final T expected) {
    assumeThat(equalTo(expected));
  }

  @Override
  public void assertThat(@Nonnull final Matcher<? super T> matcher) {
    until(new ConditionWaitImpl<T>(
            message,
            new ConditionFunction<T>(expression),
            matcher,
            timeout.in(TimeUnit.MILLISECONDS, factor),
            ASSERTION_FAIL_STRATEGY));
  }

  @Override
  public final void assertEquals(@Nullable final T expected) {
    assertThat(equalTo(expected));
  }

  @Override
  @Nonnull
  public DefaultCondition<T> runFinally(@Nullable final Runnable runnable) {
    runFinally = runnable;
    return this;
  }

  @Override
  @Nonnull
  public DefaultCondition<T> runBefore(@Nullable final Runnable runnable) {
    runBefore = runnable;
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
            .add("runBefore", runBefore)
            .add("runFinally", runFinally)
            .add("timeout", timeout)
            .add("factor", factor)
            .toString();
  }


}

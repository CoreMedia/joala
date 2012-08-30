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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p>
 * Implementation to wait for a condition to fulfill.
 * </p>
 *
 * @since 8/23/12
 */
class ConditionWaitImpl<T> implements ConditionWait {
  private static final Logger LOG = LoggerFactory.getLogger(ConditionWaitImpl.class);

  static final long INITIAL_DELAY = 10L;
  static final double DECELERATION_FACTOR = 1.1;
  private static final int TIMEOUT_DEADLINE_TOLERANCE = 100;

  @Nullable
  private final String message;
  @Nonnull
  private final ConditionFunction<T> function;
  @Nonnull
  private final Matcher<? super T> matcher;
  @Nonnull
  private final ConditionWaitFailStrategy failStrategy;
  @Nonnegative
  private final long timeoutMillis;


  ConditionWaitImpl(@Nullable final String message,
                    @Nonnull final ConditionFunction<T> function,
                    @Nonnull final Matcher<? super T> matcher,
                    @Nonnegative final long timeoutMillis,
                    @Nonnull final ConditionWaitFailStrategy failStrategy) {
    this.message = message;
    this.function = function;
    this.matcher = matcher;
    this.failStrategy = failStrategy;
    this.timeoutMillis = timeoutMillis;
  }

  /**
   * Return the current time in milliseconds. Overwrite for tests.
   *
   * @return the current time
   */
  @Nonnegative
  protected long now() {
    return System.currentTimeMillis();
  }

  /**
   * Sleep the given number of milliseconds. Overwrite for tests.
   *
   * @param millis how long to sleep
   * @throws InterruptedException if the current thread has been interrupted
   */
  protected void sleep(final long millis) throws InterruptedException {
    Thread.sleep(millis);
  }

  @Override
  public void until() {
    // Compute the deadline until which we want to wait.
    final long deadline = now() + timeoutMillis;
    // At first, wait 10ms between checks.
    long delay = INITIAL_DELAY;
    // We keep track of the last exception to be able to rethrow it.
    while (true) {
      // Measure the time that the evaluation takes.
      final long beforeEvaluation = now();
      ExpressionEvaluationException lastException;
      try {
        // Evaluate and report the result unless it is null, false, or an exception.
        if (function.apply(matcher)) {
          return;
        }
        lastException = null;
      } catch (ExpressionEvaluationException e) {
        LOG.trace("Evaluation of {} failed.", function, e);
        // Remember the exception for rethrowing.
        lastException = e;
      }
      final long now = now();
      // Are we past the deadline?
      if (now > deadline) {
        final long consumedMillis = timeoutMillis + now - deadline;
        if (lastException != null) {
          failStrategy.fail(message, function, lastException, consumedMillis);
        } else {
          failStrategy.fail(message, function, matcher, consumedMillis);
        }
      }

      // Leave at least as much time between two checks as the check itself took.
      final long lastDuration = now - beforeEvaluation;
      if (lastDuration > delay) {
        delay = lastDuration;
      }

      // Wait, but not much longer than until the deadline and at least a millisecond.
      try {
        sleep(Math.max(1, Math.min(delay, deadline + TIMEOUT_DEADLINE_TOLERANCE - now)));
      } catch (InterruptedException e) {
        throw new IllegalStateException("Unexpected Interruption", e);
      }

      // Make checks less and less frequently.
      // Increase the wait period using the deceleration factor, but
      // wait at least one millisecond longer next time.
      delay = Math.max(delay + 1, (long) (delay * DECELERATION_FACTOR));
    }
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("message", message)
            .add("function", function)
            .add("matcher", matcher)
            .add("timeoutMillis", timeoutMillis)
            .add("failStrategy", failStrategy)
            .toString();
  }


}

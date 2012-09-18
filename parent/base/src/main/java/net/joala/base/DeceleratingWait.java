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

package net.joala.base;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * A waiter that uses the exponential backoff approach when waiting for a condition.
 * Starting with a minimum wait period, the algorithm waits successively longer on
 * subsequent attempts, increasing the wait time by a small percentage. This allows
 * short waits to be completed quickly while reducing the overhead for repeated
 * checks during long waits.
 * </p><p>
 * The expected number of checks grows logarithmically with
 * the wait duration, while the wait duration remains within a constant factor of the
 * actual time until the condition holds true.
 * </p>
 */
// This class was in part derived from org.openqa.selenium.support.ui.FluentWait,
// which is available under an Apache 2.0 license.
public class DeceleratingWait implements Wait {

  private long timeout = 500L;
  private long initialDelay = 10L;
  private double decelerationFactor = 1.1;
  private String message = null;
  private final Timeout timeoutObj;

  private Collection<Class<? extends RuntimeException>> ignoredExceptionClasses = new ArrayList<Class<? extends RuntimeException>>();

  public DeceleratingWait() {
    this(new TimeoutImpl(500l, TimeUnit.MILLISECONDS));
  }

  public DeceleratingWait(final Timeout timeoutObj) {
    this.timeoutObj = timeoutObj;
  }

  /**
   * Return the current time in milliseconds. Overwrite for tests.
   *
   * @return the current time
   */
  @VisibleForTesting
  protected long nowMillis() {
    return System.currentTimeMillis();
  }

  /**
   * Sleep the given number of milliseconds.
   *
   * @param millis how long to sleep
   * @throws InterruptedException if the current thread has been interrupted
   */
  @VisibleForTesting
  protected void sleep(final long millis) throws InterruptedException {
    Thread.sleep(millis);
  }

  @Override
  public <F, T> T until(@Nonnull final F input, @Nonnull final Function<? super F, T> stateQuery, @Nonnull final Matcher<? super T> matcher) {
    // Compute the deadlineMillis until which we want to wait.
    final long deadlineMillis = nowMillis() + timeout;
    // At first, wait 10ms between checks.
    long delay = initialDelay;
    // We keep track of the last exception to be able to rethrow it.
    IgnorableStateQueryException lastException = null;
    while (true) {
      // Measure the time that the evaluation takes.
      final long beforeEvaluation = nowMillis();
      try {
        // Evaluate and report the result unless it is null, false, or an exception.
        final T result = stateQuery.apply(input);
        if (matcher.matches(result)) {
          return result;
        }
      } catch (IgnorableStateQueryException e) {
        // Remember the exception for rethrowing.
        lastException = e;
      }
      final long now = nowMillis();
      // Are we past the deadlineMillis?
      if (now > deadlineMillis) {
        // Yes.
        final StringBuilder builder = new StringBuilder();
        builder.append("Timed out after ");
        if (timeout > 5000) {
          builder.append(timeout/1000).append(" seconds");
        } else {
          builder.append(timeout).append(" milliseconds");
        }
        if (message != null) {
          builder.append(": ").append(message);
        }
        throw timeoutException(builder.toString(), lastException);
      }

      // Leave at least as much time between two checks as the check itself took.
      final long lastDuration = now - beforeEvaluation;
      if (lastDuration > delay) {
        delay = lastDuration;
      }

      // Wait, but not much longer than until the deadlineMillis and at least a millisecond.
      try {
        sleep(Math.max(1, Math.min(delay, deadlineMillis + 100 - now)));
      } catch (InterruptedException e) {
        throw new IllegalStateException("unexpected interruption", e);
      }

      // Make checks less and less frequently.
      // Increase the wait period using the deceleration factor, but
      // wait at least one millisecond longer next time.
      delay = Math.max(delay + 1, (long)(delay * decelerationFactor));
    }
  }

  @Override
  public <T> T until(final Function<? super F, T> isTrue) {
    // Compute the deadlineMillis until which we want to wait.
    final long deadlineMillis = nowMillis() + timeout;
    // At first, wait 10ms between checks.
    long delay = initialDelay;
    // We keep track of the last exception to be able to rethrow it.
    RuntimeException lastException = null;
    while (true) {
      // Measure the time that the evaluation takes.
      final long beforeEvaluation = nowMillis();
      try {
        // Evaluate and report the result unless it is null, false, or an exception.
        final T result = isTrue.apply(input);
        if (result != null && !Boolean.FALSE.equals(result)) {
          return result;
        }
      } catch (RuntimeException e) {
        // Remember the exception for rethrowing.
        lastException = propagateIfNotIngored(e);
      }
      final long now = nowMillis();
      // Are we past the deadlineMillis?
      if (now > deadlineMillis) {
        // Yes.
        final StringBuilder builder = new StringBuilder();
        builder.append("Timed out after ");
        if (timeout > 5000) {
          builder.append(timeout/1000).append(" seconds");
        } else {
          builder.append(timeout).append(" milliseconds");
        }
        if (message != null) {
          builder.append(": ").append(message);
        }
        throw timeoutException(builder.toString(), lastException);
      }

      // Leave at least as much time between two checks as the check itself took.
      final long lastDuration = now - beforeEvaluation;
      if (lastDuration > delay) {
        delay = lastDuration;
      }

      // Wait, but not much longer than until the deadlineMillis and at least a millisecond.
      try {
        sleep(Math.max(1, Math.min(delay, deadlineMillis + 100 - now)));
      } catch (InterruptedException e) {
        throw new IllegalStateException("unexpected interruption", e);
      }

      // Make checks less and less frequently.
      // Increase the wait period using the deceleration factor, but
      // wait at least one millisecond longer next time.
      delay = Math.max(delay + 1, (long)(delay * decelerationFactor));
    }
  }

  private RuntimeException propagateIfNotIngored(final RuntimeException e) {
    for (final Class<? extends RuntimeException> ignoredException : ignoredExceptionClasses) {
      if (ignoredException.isInstance(e)) {
        return e;
      }
    }
    throw e;
  }

  /**
   * Throws a timeout exception. This method may be overridden to throw an exception that is
   * idiomatic for a particular test infrastructure, such as an AssertionError in JUnit4.
   *
   * @param message The timeout message.
   * @param lastException The last exception to be thrown and subsequently supressed while waiting
   *        on a function.
   * @return Nothing will ever be returned; this return type is only specified as a convience.
   */
  protected RuntimeException timeoutException(final String message, final RuntimeException lastException) {
    throw new TimeoutException(message, lastException);
  }

  /**
   * Sets how long to wait for the evaluated condition to be true. The default timeout is
   * 500 milliseconds.
   *
   * @param duration the timeout duration
   * @param unit The unit of time.
   * @return a self reference
   */
  public DeceleratingWait<F> withTimeout(final long duration, final TimeUnit unit) {
    this.timeout = unit.toMillis(duration);
    return this;
  }

  /**
   * Sets how long to wait between two checks initially. Subsequent delays get
   * exponentially longer. The default timeout is 10 milliseconds.
   *
   * @param duration the initial delay
   * @param unit The unit of time.
   * @return a self reference
   */
  public DeceleratingWait<F> withInitialDelay(final long duration, final TimeUnit unit) {
    this.initialDelay = unit.toMillis(duration);
    return this;
  }

  /**
   * Set the deceleration factor, that is, the factor with which the delay between to checks
   * is multiplied after each check.
   * The default deceleration factor is 1.1, indicating a 10 percent increase
   * in waiting time per iteration.
   *
   * @param decelerationFactor the deceleration factor
   * @return a self reference
   */
  public DeceleratingWait<F> withDecelerationFactor(final double decelerationFactor) {
    this.decelerationFactor = decelerationFactor;
    return this;
  }

  /**
   * Sets the message to be displayed when time expires.
   *
   * @param message to be appended to default.
   * @return a self reference
   */
  public DeceleratingWait<F> withMessage(final String message) {
    this.message = message;
    return this;
  }

  /**
   * Configures this instance to ignore specific types of exceptions while waiting for a condition.
   * Any exceptions not whitelisted will be allowed to propagate, terminating the wait.
   *
   * @param types the types of exceptions to ignore
   * @return a self reference
   */
  public DeceleratingWait<F> ignoreAll(final Collection<Class<? extends RuntimeException>> types) {
    ignoredExceptionClasses.addAll(types);
    return this;
  }

  /**
   * Configures this instance to ignore specific types of exceptions while waiting for a condition.
   * Any exceptions not whitelisted will be allowed to propagate, terminating the wait.
   *
   * @param exceptionType the exception class to ignore
   * @see #ignoreAll(java.util.Collection)
   */
  public DeceleratingWait<F> ignoring(final Class<? extends RuntimeException> exceptionType) {
    ignoredExceptionClasses.add(exceptionType);
    return this;
  }

  /**
   * Configures this instance to ignore specific types of exceptions while waiting for a condition.
   * Any exceptions not whitelisted will be allowed to propagate, terminating the wait.
   *
   * @param firstType a first exception class to ignore
   * @param secondType a second exception class to ignore
   * @return
   */
  public DeceleratingWait<F> ignoring(final Class<? extends RuntimeException> firstType,
                                final Class<? extends RuntimeException> secondType) {
    ignoredExceptionClasses.add(firstType);
    ignoredExceptionClasses.add(secondType);
    return this;
  }
}

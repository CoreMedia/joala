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

package net.joala.condition.timing;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import net.joala.time.Timeout;
import net.joala.time.TimeoutImpl;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.lang.Thread.currentThread;

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
  private static final Logger LOG = LoggerFactory.getLogger(DeceleratingWait.class);

  @VisibleForTesting
  static final long DEFAULT_TIMEOUT_MILLIS = 500L;
  @VisibleForTesting
  static final long INITIAL_DELAY = 10L;
  @VisibleForTesting
  static final double DECELERATION_FACTOR = 1.1;
  private static final long SLEEP_NOT_MUCH_LONGER_OFFSET_MILLIS = 100L;
  @NonNull
  private final Timeout timeout;
  private final double timeoutFactor;
  @NonNull
  private final WaitFailStrategy failStrategy;

  public DeceleratingWait() {
    this(new TimeoutImpl(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS));
  }

  public DeceleratingWait(@NonNull final Timeout timeout) {
    this(timeout, new WaitTimeoutFailStrategy());
  }

  public DeceleratingWait(@NonNull final Timeout timeout, final double timeoutFactor) {
    this(timeout, timeoutFactor, new WaitTimeoutFailStrategy());
  }

  public DeceleratingWait(@NonNull final Timeout timeout, @NonNull final WaitFailStrategy failStrategy) {
    this(timeout, 1d, failStrategy);
  }

  public DeceleratingWait(@NonNull final Timeout timeout, final double timeoutFactor, @NonNull final WaitFailStrategy failStrategy) {
    this.timeout = timeout;
    this.timeoutFactor = timeoutFactor;
    this.failStrategy = failStrategy;
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
  public final <F, T> T until(@NonNull final F input, @NonNull final Function<? super F, T> stateQuery) {
    return until(null, input, stateQuery, null);
  }

  @Override
  public <F, T> T until(@NonNull final F input, @NonNull final Function<? super F, T> stateQuery, @Nullable final Matcher<? super T> matcher) {
    return until(null, input, stateQuery, matcher);
  }

  @Override
  public <F, T> T until(@Nullable final String message,
                        @NonNull final F input,
                        @NonNull final Function<? super F, T> stateQuery,
                        @Nullable final Matcher<? super T> matcher) {
    // Compute the deadlineTimeMillis until which we want to wait.
    final long startTimeMillis = nowMillis();
    final long timeoutMillis = timeout.in(TimeUnit.MILLISECONDS, timeoutFactor);
    final long deadlineTimeMillis = startTimeMillis + timeoutMillis;
    if (LOG.isDebugEnabled()) {
      LOG.debug("Start waiting for:");
      LOG.debug("  state query: .... {}", stateQuery);
      LOG.debug("  matcher: ........ {}", matcher);
      LOG.debug("  timeout (ms): ... {}", timeoutMillis);
    }
    // At first, wait 10ms between checks.
    long delay = INITIAL_DELAY;
    // We keep track of the last exception to be able to rethrow it.
    IgnorableStateQueryException lastException = null;
    T lastState = null;
    while (true) {
      // Measure the time that the evaluation takes.
      final long beforeEvaluationTimeMillis = nowMillis();
      try {
        // Evaluate and report the result unless it is null, false, or an exception.
        final T result = stateQuery.apply(input);
        if (matcher == null || matcher.matches(result)) {
          return result;
        }
        lastState = result;
      } catch (IgnorableStateQueryException e) {
        // Remember the exception for rethrowing.
        LOG.trace("Ignoring exception for now. Might rethrow later if failed with error.", e);
        lastException = e;
      }
      final long afterEvaluationTimeMillis = nowMillis();
      // Are we past the deadlineTimeMillis?
      if (afterEvaluationTimeMillis > deadlineTimeMillis) {
        failAtDeadline(message, stateQuery, input, lastException, lastState, matcher, startTimeMillis);
      }
      delay = sleepAndRecalculateDelay(delay, deadlineTimeMillis, beforeEvaluationTimeMillis, afterEvaluationTimeMillis);
    }
  }

  private long sleepAndRecalculateDelay(final long previousDelay, final long deadlineTimeMillis, final long beforeEvaluationTimeMillis, final long afterEvaluationTimeMillis) {
    long newDelay = previousDelay;
    // Leave at least as much time between two checks as the check itself took.
    final long lastDuration = afterEvaluationTimeMillis - beforeEvaluationTimeMillis;
    if (lastDuration > newDelay) {
      newDelay = lastDuration;
    }

    // Wait, but not much longer than until the deadlineTimeMillis and at least a millisecond.
    try {
      sleep(Math.max(1, Math.min(newDelay, deadlineTimeMillis + SLEEP_NOT_MUCH_LONGER_OFFSET_MILLIS - afterEvaluationTimeMillis)));
    } catch (InterruptedException e) {
      LOG.error("Unexpected interruption.", e);
      currentThread().interrupt();
    }

    // Make checks less and less frequently.
    // Increase the wait period using the deceleration factor, but
    // wait at least one millisecond longer next time.
    newDelay = Math.max(newDelay + 1, (long) (newDelay * DECELERATION_FACTOR));
    return newDelay;
  }

  private <F, T> void failAtDeadline(@Nullable final String message,
                                     @NonNull final Function<? super F, T> stateQuery,
                                     @NonNull final F input,
                                     @Nullable final IgnorableStateQueryException lastException,
                                     @Nullable final T lastState,
                                     @NonNull final Matcher<? super T> matcher,
                                     final long startTimeMillis) {
    final long consumedMillis = nowMillis() - startTimeMillis;
    if (lastException == null) {
      failStrategy.fail(message, stateQuery, input, lastState, matcher, consumedMillis);
    } else {
      failStrategy.fail(message, stateQuery, input, lastException, consumedMillis);
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
                      .add("timeout", timeout)
                      .add("timeoutFactor", timeoutFactor)
                      .add("failStrategy", failStrategy)
                      .toString();
  }
}

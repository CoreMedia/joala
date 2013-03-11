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

import org.hamcrest.Matcher;
import org.junit.internal.AssumptionViolatedException;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.junit.Assume.assumeThat;

/**
 * <p>
 * Fail-strategy to fail with timeout exception.
 * </p>
 *
 * @since 8/23/12
 */
public class WaitTimeoutFailStrategy extends AbstractWaitFailStrategy {
  @Override
  public void fail(@Nullable final String reason,
                   @Nonnull final Object function,
                   @Nonnull final Object input,
                   @Nonnull final Throwable exception,
                   @Nonnegative final long consumedMillis) {
    throw new WaitTimeoutException(addTimeoutDescription(reason, function, input, consumedMillis), exception);
  }

  @Override
  public <T> void fail(@Nullable final String reason,
                       @Nonnull final Object function,
                       @Nonnull final Object input,
                       @Nullable final T lastValue,
                       @Nonnull final Matcher<? super T> matcher,
                       @Nonnegative final long consumedMillis) {
    try {
      assumeThat(lastValue, matcher);
    } catch (AssumptionViolatedException e) {
      throw new WaitTimeoutException(addTimeoutDescription(reason, function, input, consumedMillis), e);
    }
  }
}

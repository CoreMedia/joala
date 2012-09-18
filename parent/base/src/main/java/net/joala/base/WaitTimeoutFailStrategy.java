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

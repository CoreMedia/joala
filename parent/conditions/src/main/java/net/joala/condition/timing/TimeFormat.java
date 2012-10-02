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

package net.joala.condition.timing;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * @since 9/18/12
 * @deprecated since 0.5.0; Use {@link net.joala.time.TimeFormat} instead
 */
@Deprecated
public final class TimeFormat {
  private TimeFormat() {
  }

  /**
   * <p>
   * Format the given time amount in a "human readable" way.
   * </p>
   *
   * @param amount   amount of time units
   * @param timeUnit the time unit
   * @return time amount in human readable format (for example converted to seconds or minutes)
   */
  public static String format(@Nonnegative final long amount, @Nonnull final TimeUnit timeUnit) {
    return net.joala.time.TimeFormat.format(amount, timeUnit);
  }
}

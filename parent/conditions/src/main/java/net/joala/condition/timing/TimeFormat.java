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
 */
public final class TimeFormat {
  private static final int TIMEUNIT_LIMIT = 2;

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
    final long formatAmount;
    final String formatUnit;
    if (timeUnit.toHours(amount) >= TIMEUNIT_LIMIT) {
      formatAmount = timeUnit.toHours(amount);
      formatUnit = "h";
    } else if (timeUnit.toMinutes(amount) >= TIMEUNIT_LIMIT) {
      formatAmount = timeUnit.toMinutes(amount);
      formatUnit = "min";
    } else if (timeUnit.toSeconds(amount) >= TIMEUNIT_LIMIT) {
      formatAmount = timeUnit.toSeconds(amount);
      formatUnit = "s";
    } else {
      formatAmount = amount;
      formatUnit = "ms";
    }
    return String.format("%d %s", formatAmount, formatUnit);
  }
}

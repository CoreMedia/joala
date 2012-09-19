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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @since 9/18/12
 */
public final class TimeFormat {
  @VisibleForTesting
  static final int TIMEUNIT_LIMIT = 2;

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
    final List<TimeUnit> reverse = Lists.reverse(Arrays.asList(TimeUnit.values()));
    for (final TimeUnit currentUnit : reverse) {
      if (currentUnit.equals(timeUnit) || currentUnit.convert(amount, timeUnit) >= TIMEUNIT_LIMIT) {
        return TimeUnitFormat.getFormat(currentUnit).format(amount, timeUnit);
      }
    }
    throw new IllegalArgumentException(String.format("TimeUnit %s not supported for formatting.", timeUnit));
  }

  // UnusedDeclaration: enum elements are not used directly but by name matching
  // with TimeUnit-element-names. Thus static code analysis will report the enum
  // elements to be unused.
  @SuppressWarnings("UnusedDeclaration")
  private enum TimeUnitFormat {
    NANOSECONDS("ns", TimeUnit.NANOSECONDS),
    MICROSECONDS("µs", TimeUnit.MICROSECONDS),
    MILLISECONDS("ms", TimeUnit.MILLISECONDS),
    SECONDS("s", TimeUnit.SECONDS),
    MINUTES("min", TimeUnit.MINUTES),
    HOURS("h", TimeUnit.HOURS),
    DAYS("d", TimeUnit.DAYS);

    private final String sign;
    private final TimeUnit timeUnit;

    TimeUnitFormat(final String sign, final TimeUnit timeUnit) {
      this.sign = sign;
      this.timeUnit = timeUnit;
    }

    public String format(final long amount, final TimeUnit sourceUnit) {
      return String.format("%d %s", timeUnit.convert(amount, sourceUnit), sign);
    }

    public static TimeUnitFormat getFormat(final TimeUnit timeUnit) {
      // Simple matching by name; throws IllegalArgumentException if there is no such name.
      return TimeUnitFormat.valueOf(timeUnit.name());
    }
  }
}

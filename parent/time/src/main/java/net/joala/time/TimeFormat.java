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

package net.joala.time;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import edu.umd.cs.findbugs.annotations.NonNull;

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
  public static String format(final long amount, @NonNull final TimeUnit timeUnit) {
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

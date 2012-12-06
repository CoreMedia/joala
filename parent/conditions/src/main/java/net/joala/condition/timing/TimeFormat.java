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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.condition.timing;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * @since 9/18/12
 * @deprecated since 0.5.0; Use {@link net.joala.time.TimeFormat} instead
 */
@SuppressWarnings("UnusedDeclaration")
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
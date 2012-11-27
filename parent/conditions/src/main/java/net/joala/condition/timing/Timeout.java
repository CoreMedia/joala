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
 * <p>
 * A timeout which can be converted to different time-units.
 * </p>
 *
 * @since 8/22/12
 * @deprecated since 0.5.0; use {@link net.joala.time.Timeout} instead
 */
@Deprecated
public interface Timeout {
  /**
   * Get the timeout in the given unit.
   *
   * @param targetUnit the timeunit to use
   * @return timeout in the given unit
   */
  @Nonnegative
  @SuppressWarnings("PMD.ShortMethodName")
  long in(@Nonnull TimeUnit targetUnit);

  /**
   * <p>
   * Get the timeout in the given unit adjusted by the given factor.
   * </p>
   *
   * @param targetUnit the timeunit to use
   * @param factor     factor to adjust the timeout
   * @return timeout adjust by factor
   */
  @Nonnegative
  @SuppressWarnings("PMD.ShortMethodName")
  long in(@Nonnull TimeUnit targetUnit, @Nonnegative double factor);
}

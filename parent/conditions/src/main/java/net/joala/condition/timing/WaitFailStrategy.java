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

import org.hamcrest.Matcher;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p>
 * Strategy what to do if a condition does not get fulfilled within time. Possible options
 * are to signal just the timeout or that an assertion/assumption failed.
 * </p>
 *
 * @since 8/23/12
 */
public interface WaitFailStrategy {
  /**
   * Makes a condition fail because the expected value did not get returned in time.
   *
   * @param reason         reason of the failure
   * @param function       function evaluated
   * @param input          original input to the function
   * @param lastValue      the last value retrieved via function
   * @param matcher        the matcher which did not match
   * @param consumedMillis consumed milliseconds
   * @param <T>            the value type returned by the function
   */
  <T> void fail(@Nullable String reason,
                @Nonnull Object function,
                @Nonnull Object input,
                @Nullable T lastValue,
                @Nonnull Matcher<? super T> matcher,
                @Nonnegative long consumedMillis);

  /**
   * Makes a condition fail because the expected value could not be retrieved because of repeating
   * evaluation exceptions.
   *
   * @param reason         reason of the failure
   * @param function       function evaluated
   * @param input          original input to the function
   * @param throwable      last exception which got caught
   * @param consumedMillis consumed milliseconds
   */
  void fail(@Nullable String reason,
            @Nonnull Object function,
            @Nonnull Object input,
            @Nonnull Throwable throwable,
            @Nonnegative long consumedMillis);
}

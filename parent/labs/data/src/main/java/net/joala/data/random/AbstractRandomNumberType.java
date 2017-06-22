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

package net.joala.data.random;

import com.google.common.base.Objects;
import com.google.common.collect.Range;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * Abstract implementation of RandomNumberType holding the type of numbers to create.
 * </p>
 *
 * @param <T> number type to generate
 * @since 9/17/12
 */
public abstract class AbstractRandomNumberType<T extends Comparable<? extends Number>> implements RandomNumberType<T> {
  private static final Range<Double> VALID_PERCENTAGE_RANGE = Range.closed(0d, 1d);
  /**
   * Type of numbers to create.
   */
  @Nonnull
  private final Class<T> type;

  /**
   * <p>
   * Constructor specifying type of random numbers.
   * </p>
   *
   * @param type type of numbers
   */
  protected AbstractRandomNumberType(@Nonnull final Class<T> type) {
    checkNotNull(type, "Type must not be null.");
    this.type = type;
  }

  @Override
  @Nonnull
  public final Class<T> getType() {
    return type;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("type", type)
            .toString();
  }

  /**
   * Verifies that the given percentage is between 0 and 1.
   *
   * @param percent percentage value to check
   * @throws IllegalArgumentException if percentage is out of the valid bounds
   */
  protected void checkPercentageArgument(final double percent) {
    checkArgument(VALID_PERCENTAGE_RANGE.contains(percent),
            "Given percentage must be between 0 and 1 but is %s.",
            percent);
  }
}

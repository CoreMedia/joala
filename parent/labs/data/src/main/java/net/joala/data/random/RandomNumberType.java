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

import javax.annotation.Nonnull;

/**
 * <p>
 *   Describes random numbers to create. This interface is required by {@link AbstractRandomNumberProvider}
 *   in order to calculate a random number in the given range.
 * </p>
 * @since 9/14/12
 */
public interface RandomNumberType<T extends Comparable<? extends Number>> {
  /**
   * Type of the number.
   * @return the type of numbers to create
   */
  @Nonnull
  Class<T> getType();

  /**
   * <p>
   * The minimum value supported for this number type. Typically used as default minimum value.
   * </p>
   * @return minimum number which might be created
   */
  @Nonnull
  T min();

  /**
   * <p>
   *   The maximum value supported for this number type. Typically used as default maximum value.
   * </p>
   * @return maximum number which might be created
   */
  @Nonnull
  T max();

  /**
   * <p>
   * Create the sum of the two values.
   * </p>
   * @param value1 first value
   * @param value2 second value
   * @return sum
   */
  @Nonnull
  T sum(T value1, T value2);

  /**
   * <p>
   *   Return the percentage of the given value. Typically used to locate a number in a given range on the
   *   provided numerical ray.
   * </p>
   * @param percent percent ranging between 0 and 1
   * @param value value to calculate the percentage of
   * @return percentage of given value
   */
  @Nonnull
  T percentOf(double percent, T value);
}

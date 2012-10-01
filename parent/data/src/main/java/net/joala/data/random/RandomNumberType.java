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

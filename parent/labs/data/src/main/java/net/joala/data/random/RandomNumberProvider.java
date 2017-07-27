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

import javax.annotation.Nullable;

/**
 * <p>
 * Provides random numbers. Configuration methods always return self-references.
 * </p>
 *
 * @deprecated Will be removed soon.
 */
@SuppressWarnings("deprecation")
@Deprecated
public interface RandomNumberProvider<T extends Comparable<? extends Number>> extends RandomDataProvider<T> {
  /**
   * <p>
   * Specifies the minimum value to retrieve via random.
   * </p>
   *
   * @param minValue the minimum value; {@code null} will cause to reset to minimum value of number type
   * @return self-reference
   */
  RandomNumberProvider<T> min(@Nullable T minValue);

  /**
   * <p>
   * Specifies the maximum value to retrieve via random.
   * </p>
   *
   * @param maxValue the maximum value; {@code null} will cause to reset to maximum value of number type
   * @return self-reference
   */
  RandomNumberProvider<T> max(@Nullable T maxValue);
}

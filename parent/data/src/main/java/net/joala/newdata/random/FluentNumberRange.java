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

package net.joala.newdata.random;

import javax.annotation.Nonnull;

/**
 * <p>
 * Fluent interface for configuring number ranges for random numbers.
 * </p>
 * <p>
 * <strong>Implementing Interface</strong>
 * </p>
 * <ul>
 * <li>Override the given methods by a return type equal to the implementing class,</li>
 * <li>{@code null} is reserved as special value for <em>uninitialized</em></li>
 * <li><em>uninitialized</em> means to use a convenient default value.</li>
 * </ul>
 *
 * @since 10/23/12
 */
public interface FluentNumberRange<T extends Number> {

  /**
   * <p>
   * Set minimum value the random numbers might reach.
   * </p>
   * @param minValue minimum value
   * @return self reference
   */
  @Nonnull
  FluentNumberRange<T> min(@Nonnull T minValue);

  /**
   * <p>
   * Set maximum value the random numbers might reach.
   * </p>
   * @param maxValue maximum value
   * @return self reference
   */
  @Nonnull
  FluentNumberRange<T> max(@Nonnull T maxValue);
}

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

/**
 * <p>
 * Factories to create wait strategies.
 * </p>
 *
 * @since 0.8.0
 */
public interface WaitFactory {
  /**
   * <p>
   * Creates a wait strategy with the given tolerance when waiting. Tolerance for example
   * can be implemented as factor for a given timeout. Thus a value of 0.5 might make the
   * waiter more impatient while a value 2.0 might increase the timeout by the given factor.
   * </p>
   *
   * @param tolerance    tolerance to apply when waiting
   * @param failStrategy strategy to use for failing
   * @return waiter
   */
  @Nonnull
  Wait get(@Nonnegative final double tolerance, @Nonnull final WaitFailStrategy failStrategy);
}

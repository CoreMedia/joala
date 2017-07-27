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
 * Random number provider for integer values.
 * </p>
 *
 * @deprecated Will be removed soon.
 */
@SuppressWarnings("deprecation")
@Deprecated
public class RandomIntegerProvider extends AbstractRandomNumberProvider<Integer> {
  /**
   * <p>
   * Creates a random number provider for integer values.
   * </p>
   */
  public RandomIntegerProvider() {
    super(new IntegerRandomNumberType());
  }

  /**
   * <p>
   * Describe the integer number type as needed to provide random data.
   * </p>
   */
  private static final class IntegerRandomNumberType extends AbstractRandomNumberType<Integer> {

    private IntegerRandomNumberType() {
      super(Integer.class);
    }

    @Override
    @Nonnull
    public Integer min() {
      return Integer.MIN_VALUE;
    }

    @Override
    @Nonnull
    public Integer max() {
      return Integer.MAX_VALUE;
    }

    @Override
    @Nonnull
    public Integer sum(final Integer value1, final Integer value2) {
      return value1 + value2;
    }

    @Override
    @Nonnull
    public Integer percentOf(final double percent, final Integer value) {
      checkPercentageArgument(percent);
      return (int) percent * value;
    }
  }
}

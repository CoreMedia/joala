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
 * Random number provider for double values.
 * </p>
 *
 * @since 9/17/12
 */
@Deprecated
public class RandomDoubleProvider extends AbstractRandomNumberProvider<Double> {
  /**
   * <p>
   * Creates a random number provider for double values.
   * </p>
   */
  public RandomDoubleProvider() {
    super(new DoubleRandomNumberType());
  }

  /**
   * <p>
   * Describe the double number type as needed to provide random data.
   * </p>
   */
  private static final class DoubleRandomNumberType extends AbstractRandomNumberType<Double> {

    private DoubleRandomNumberType() {
      super(Double.class);
    }

    @Override
    @Nonnull
    public Double min() {
      return Double.MIN_VALUE;
    }

    @Override
    @Nonnull
    public Double max() {
      return Double.MAX_VALUE;
    }

    @Override
    @Nonnull
    public Double sum(final Double value1, final Double value2) {
      return value1 + value2;
    }

    @Override
    @Nonnull
    public Double percentOf(final double percent, final Double value) {
      checkPercentageArgument(percent);
      return percent * value;
    }

  }
}

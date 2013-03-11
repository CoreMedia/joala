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
 * Random number provider for float values.
 * </p>
 *
 * @since 9/17/12
 */
public class RandomFloatProvider extends AbstractRandomNumberProvider<Float> {
  /**
   * <p>
   * Creates a random number provider for float values.
   * </p>
   */
  public RandomFloatProvider() {
    super(new FloatRandomNumberType());
  }

  /**
   * <p>
   * Describe the float number type as needed to provide random data.
   * </p>
   */
  private static final class FloatRandomNumberType extends AbstractRandomNumberType<Float> {

    private FloatRandomNumberType() {
      super(Float.class);
    }

    @Override
    @Nonnull
    public Float min() {
      return Float.MIN_VALUE;
    }

    @Override
    @Nonnull
    public Float max() {
      return Float.MAX_VALUE;
    }

    @Override
    @Nonnull
    public Float sum(final Float value1, final Float value2) {
      return value1 + value2;
    }

    @Override
    @Nonnull
    public Float percentOf(final double percent, final Float value) {
      checkPercentageArgument(percent);
      return (float) percent * value;
    }
  }
}

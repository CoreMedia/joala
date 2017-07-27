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
 * Random number provider for long values.
 * </p>
 *
 * @deprecated Will be removed soon.
 */
@SuppressWarnings("deprecation")
@Deprecated
public class RandomLongProvider extends AbstractRandomNumberProvider<Long> {
  /**
   * <p>
   * Creates a random number provider for long values.
   * </p>
   */
  public RandomLongProvider() {
    super(new LongRandomNumberType());
  }

  /**
   * <p>
   * Describe the long number type as needed to provide random data.
   * </p>
   */
  private static final class LongRandomNumberType extends AbstractRandomNumberType<Long> {

    private LongRandomNumberType() {
      super(Long.class);
    }

    @Override
    @Nonnull
    public Long min() {
      return Long.MIN_VALUE;
    }

    @Override
    @Nonnull
    public Long max() {
      return Long.MAX_VALUE;
    }

    @Override
    @Nonnull
    public Long sum(final Long value1, final Long value2) {
      return value1 + value2;
    }

    @Override
    @Nonnull
    public Long percentOf(final double percent, final Long value) {
      checkPercentageArgument(percent);
      return (long) percent * value;
    }
  }
}

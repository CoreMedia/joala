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
 * Random number provider for integer values.
 * </p>
 *
 * @since 9/17/12
 */
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

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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import net.joala.data.DataProvidingException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * <p>
 * Abstract data provider for numbers. Numbers are expected to be comparable.
 * </p>
 *
 * @param <T> the number type you can retrieve from this data provider
 * @since 9/17/12
 */
public abstract class AbstractRandomNumberProvider<T extends Comparable<? extends Number>> extends AbstractRandomDataProvider<T> implements RandomNumberProvider<T> {
  /**
   * Random number generator.
   */
  private static final Random GENERATOR = new Random(System.currentTimeMillis());

  /**
   * Minimum value for numbers. If unspecified the minimum supported value for the number type will be used.
   */
  @Nonnull
  private T minValue;
  /**
   * Maximum value for numbers. If unspecified the maximum supported value for the number type will be used.
   */
  @Nonnull
  private T maxValue;
  /**
   * Reference for the type of random numbers to provide.
   */
  @Nonnull
  private final RandomNumberType<T> numberType;

  /**
   * <p>
   * Creates a random number provider bound to a specific type of numbers.
   * </p>
   *
   * @param numberType denotes the type of random numbers to provide
   */
  protected AbstractRandomNumberProvider(@Nonnull final RandomNumberType<T> numberType) {
    checkNotNull(numberType, "Number Type must not be null.");
    this.numberType = numberType;
    this.maxValue = numberType.max();
    this.minValue = numberType.min();
  }

  @Override
  public RandomNumberProvider<T> min(@Nullable final T minValue) {
    if (minValue == null) {
      this.minValue = numberType.min();
    } else {
      this.minValue = minValue;
    }
    return this;
  }

  @Override
  public RandomNumberProvider<T> max(@Nullable final T maxValue) {
    if (maxValue == null) {
      this.maxValue = numberType.max();
    } else {
      this.maxValue = maxValue;
    }
    return this;
  }

  @Override
  public T get() throws DataProvidingException {
    try {
      return nextRandom(Ranges.closed(minValue, maxValue));
    } catch (IllegalArgumentException e) {
      throw new DataProvidingException("Illegal range.", e);
    }
  }

  /**
   * Provide a random number in the given range.
   * @param range (closed) range in which to choose a number from
   * @return random value
   * @throws IllegalStateException if range is empty
   */
  private T nextRandom(final Range<T> range) {
    checkState(!range.isEmpty(), "Range must not be empty.");
    final T lowerEndpoint = range.lowerEndpoint();
    final T upperEndpoint = range.upperEndpoint();
    final double random = nextRandomRatio();
    return numberType.sum(numberType.percentOf(random, upperEndpoint), numberType.percentOf((1d - random), lowerEndpoint));
  }

  /**
   * <p>
   *   Return the number type used for random number generation.
   * </p>
   * @return random number type
   */
  @Nonnull
  public final RandomNumberType<T> getNumberType() {
    return numberType;
  }

  @VisibleForTesting
  double nextRandomRatio() {
    return GENERATOR.nextDouble();
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("numberType", numberType)
            .add("minValue", minValue)
            .add("maxValue", maxValue)
            .toString();
  }
}

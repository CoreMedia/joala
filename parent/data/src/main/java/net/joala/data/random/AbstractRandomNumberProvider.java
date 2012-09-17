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

import java.util.Random;

import static com.google.common.base.Preconditions.checkState;

/**
 * @since 9/17/12
 */
public abstract class AbstractRandomNumberProvider<T extends Comparable<? extends Number>> extends AbstractRandomDataProvider<T> implements RandomNumberProvider<T> {
  /**
   * Random number generator.
   */
  private static final Random GENERATOR = new Random(System.currentTimeMillis());

  private T minValue;
  private T maxValue;
  private final RandomNumberType<T> numberType;

  /**
   * <p>
   * Creates a random number provider bound to a specific type of numbers.
   * </p>
   *
   * @param numberType denotes the type of random numbers to provide
   */
  protected AbstractRandomNumberProvider(final RandomNumberType<T> numberType) {
    this.numberType = numberType;
    this.maxValue = numberType.max();
    this.minValue = numberType.min();
  }

  @Override
  public RandomNumberProvider<T> min(final T minValue) {
    this.minValue = minValue;
    return this;
  }

  @Override
  public RandomNumberProvider<T> max(final T maxValue) {
    this.maxValue = maxValue;
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

  private T nextRandom(final Range<T> range) {
    checkState(!range.isEmpty(), "Range must not be empty.");
    final T lowerEndpoint = range.lowerEndpoint();
    final T upperEndpoint = range.upperEndpoint();
    final double random = nextRandomRatio();
    return numberType.sum(numberType.percentOf(random, upperEndpoint), numberType.percentOf((1d - random), lowerEndpoint));
  }

  public RandomNumberType<T> getNumberType() {
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

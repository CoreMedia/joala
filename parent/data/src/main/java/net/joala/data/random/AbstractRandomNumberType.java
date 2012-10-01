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

import com.google.common.base.Objects;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * Abstract implementation of RandomNumberType holding the type of numbers to create.
 * </p>
 *
 * @param <T> number type to generate
 * @since 9/17/12
 */
public abstract class AbstractRandomNumberType<T extends Comparable<? extends Number>> implements RandomNumberType<T> {
  private static final Range<Double> VALID_PERCENTAGE_RANGE = Ranges.closed(0d, 1d);
  /**
   * Type of numbers to create.
   */
  @Nonnull
  private final Class<T> type;

  /**
   * <p>
   * Constructor specifying type of random numbers.
   * </p>
   *
   * @param type type of numbers
   */
  protected AbstractRandomNumberType(@Nonnull final Class<T> type) {
    checkNotNull(type, "Type must not be null.");
    this.type = type;
  }

  @Override
  @Nonnull
  public final Class<T> getType() {
    return type;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("type", type)
            .toString();
  }

  /**
   * Verifies that the given percentage is between 0 and 1.
   *
   * @param percent percentage value to check
   * @throws IllegalArgumentException if percentage is out of the valid bounds
   */
  protected void checkPercentageArgument(final double percent) {
    checkArgument(VALID_PERCENTAGE_RANGE.contains(percent),
            "Given percentage must be between 0 and 1 but is %s.",
            percent);
  }
}

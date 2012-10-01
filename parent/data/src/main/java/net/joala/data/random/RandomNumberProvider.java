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

import javax.annotation.Nullable;

/**
 * <p>
 * Provides random numbers. Configuration methods always return self-references.
 * </p>
 *
 * @since 9/17/12
 */
public interface RandomNumberProvider<T extends Comparable<? extends Number>> extends RandomDataProvider<T> {
  /**
   * <p>
   * Specifies the minimum value to retrieve via random.
   * </p>
   *
   * @param minValue the minimum value; {@code null} will cause to reset to minimum value of number type
   * @return self-reference
   */
  RandomNumberProvider<T> min(@Nullable T minValue);

  /**
   * <p>
   * Specifies the maximum value to retrieve via random.
   * </p>
   *
   * @param maxValue the maximum value; {@code null} will cause to reset to maximum value of number type
   * @return self-reference
   */
  RandomNumberProvider<T> max(@Nullable T maxValue);
}

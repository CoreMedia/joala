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

import net.joala.data.DataProvider;

import javax.annotation.Nonnull;

/**
 * <p>
 * Provides random data. Random generators will provide configuration methods which
 * again return self-references to build up chained-call configuration.
 * </p>
 *
 * @since 9/17/12
 */
public interface RandomDataProvider<T> extends DataProvider<T> {
  /**
   * <p>
   * Disbands any (obvious) configuration options. Implementations
   * might decide if the resulting object is immutable or not.
   * </p>
   *
   * @return a random data provider without any more configuration API
   */
  // returning only the DataProvider here also prohibits multiple calls to fixate().
  @Nonnull
  DataProvider<T> fixate();
}

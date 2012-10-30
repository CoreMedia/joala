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

package net.joala.newdata;

import javax.inject.Provider;

/**
 * <p>
 * Interface for providing test data of the given type.
 * </p>
 *
 * @param <T> the type of data returned
 * @since 9/16/12
 */
public interface DataProvider<T> extends Provider<T> {
  /**
   * Retrieve test data.
   *
   * @return random data
   * @throws DataProvidingException if an error occurred during data generation
   */
  @Override
  T get();
}

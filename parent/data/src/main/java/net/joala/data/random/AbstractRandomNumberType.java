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

/**
 * @since 9/17/12
 */
public abstract class AbstractRandomNumberType<T extends Comparable<? extends Number>> implements RandomNumberType<T> {
  private final Class<T> type;

  protected AbstractRandomNumberType(final Class<T> type) {
    this.type = type;
  }

  @Override
  public final Class<T> getType() {
    return type;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("type", type)
            .toString();
  }
}

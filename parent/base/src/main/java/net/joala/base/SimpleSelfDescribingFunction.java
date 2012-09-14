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

package net.joala.base;

import com.google.common.base.Function;
import org.hamcrest.Description;

import javax.annotation.Nullable;

/**
 * <p>
 * Implementation of a function which is able to describe itself with a simple description.
 * </p>
 * <p>
 * <strong>Feature:</strong> if you provide {@code null} as simple description this function
 * will not add anything to the description on {@link #describeTo(Description)}.
 * </p>
 *
 * @since 9/14/12
 */
public abstract class SimpleSelfDescribingFunction<F, T> extends SimpleSelfDescribing implements Function<F, T> {
  /**
   * <p>
   * Constructor providing a simple description of this function.
   * </p>
   *
   * @param simpleDescription description; if {@code null} will not add any description
   */
  protected SimpleSelfDescribingFunction(@Nullable final String simpleDescription) {
    super(simpleDescription);
  }
}

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

package net.joala.condition;

import net.joala.condition.timing.SimpleSelfDescribing;
import org.hamcrest.Description;

import javax.annotation.Nullable;

/**
 * <p>
 * Convenience abstract implementation of {@link Expression} which has an empty
 * implementation of {@link #describeTo(Description)}.
 * </p>
 *
 * @param <T> the result type of the expression
 * @since 2/27/12
 */
public abstract class AbstractExpression<T> extends SimpleSelfDescribing implements Expression<T> {
  /**
   * <p>
   * Constructor providing an empty description.
   * </p>
   */
  protected AbstractExpression() {
    this(null);
  }

  /**
   * <p>
   * Constructor providing a plain text description of this expression for debugging purpose.
   * </p>
   *
   * @param simpleDescription the simple description
   */
  protected AbstractExpression(@Nullable final String simpleDescription) {
    super(simpleDescription);
  }
}

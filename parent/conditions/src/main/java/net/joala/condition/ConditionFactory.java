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

import net.joala.expression.Expression;

import javax.annotation.Nonnull;

/**
 * <p>
 * Interface for factories providing conditions.
 * </p>
 *
 * @since 8/22/12
 */
public interface ConditionFactory {
  /**
   * <p>
   * Create a boolean condition which allows some convenience methods for assertions and assumptions.
   * </p>
   *
   * @param expression the expression which will evaluate to a boolean value
   * @return boolean condition
   * @throws NullPointerException if expression is null
   */
  @Nonnull
  BooleanCondition booleanCondition(@Nonnull Expression<Boolean> expression);

  /**
   * <p>
   * Create a ready to use condition.
   * </p>
   *
   * @param expression the expression which will evaluate to value of type T
   * @param <T>        the type the expression value will have
   * @return condition
   * @throws NullPointerException if expression is null
   */
  @Nonnull
  <T> Condition<T> condition(@Nonnull Expression<T> expression);
}

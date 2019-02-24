/*
 * Copyright 2012 CoreMedia AG
 *
 * This file is part of Joala.
 *
 * Joala is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Joala is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Joala.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.condition;

import edu.umd.cs.findbugs.annotations.NonNull;
import net.joala.expression.Expression;

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
  @NonNull
  BooleanCondition booleanCondition(@NonNull Expression<Boolean> expression);

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
  @NonNull
  <T> Condition<T> condition(@NonNull Expression<T> expression);
}

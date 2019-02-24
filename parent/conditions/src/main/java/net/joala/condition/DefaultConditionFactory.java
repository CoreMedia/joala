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
import net.joala.time.Timeout;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * Default implementation of {@link ConditionFactory}.
 * </p>
 *
 * @since 9/3/12
 */
public class DefaultConditionFactory implements ConditionFactory {
  @NonNull
  private final Timeout timeout;

  /**
   * <p>
   * Create factory for conditions with the given timeout behavior.
   * </p>
   *
   * @param timeout the timeout behavior (i. e. the default time to time out)
   */
  public DefaultConditionFactory(@NonNull final Timeout timeout) {
    this.timeout = timeout;
  }

  @NonNull
  @Override
  public BooleanCondition booleanCondition(@NonNull final Expression<Boolean> expression) {
    checkNotNull(expression, "Expression must not be null");
    return new DefaultBooleanCondition(expression, timeout);
  }

  @NonNull
  @Override
  public <T> Condition<T> condition(@NonNull final Expression<T> expression) {
    checkNotNull(expression, "Expression must not be null");
    return new DefaultCondition<>(expression, timeout);
  }
}

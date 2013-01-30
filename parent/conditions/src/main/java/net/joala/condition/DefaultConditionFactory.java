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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.condition;

import com.google.common.base.Objects;
import net.joala.condition.timing.DeceleratingWaitFactory;
import net.joala.condition.timing.WaitFactory;
import net.joala.expression.Expression;
import net.joala.time.Timeout;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * Default implementation of {@link ConditionFactory}.
 * </p>
 *
 * @since 9/3/12
 */
public class DefaultConditionFactory implements ConditionFactory {
  @Nonnull
  private final WaitFactory waitFactory;

  /**
   * <p>
   * Create factory for conditions with the given timeout behavior.
   * </p>
   *
   * @param timeout the timeout behavior (i. e. the default time to time out)
   */
  public DefaultConditionFactory(@Nonnull final Timeout timeout) {
    this(new DeceleratingWaitFactory(timeout));
  }

  /**
   * <p>
   * Create factory for conditions with the given wait strategies as provided by the factory.
   * </p>
   *
   * @param waitFactory the factory to use to retrieve wait strategies
   * @since 0.8.0
   */
  public DefaultConditionFactory(@Nonnull final WaitFactory waitFactory) {
    this.waitFactory = waitFactory;
  }

  @Nonnull
  @Override
  public BooleanCondition booleanCondition(@Nonnull final Expression<Boolean> expression) {
    checkNotNull(expression, "Expression must not be null");
    return new DefaultBooleanCondition(expression, waitFactory);
  }

  @Nonnull
  @Override
  public <T> Condition<T> condition(@Nonnull final Expression<T> expression) {
    checkNotNull(expression, "Expression must not be null");
    return new DefaultCondition<T>(expression, waitFactory);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("waitFactory", waitFactory)
            .toString();
  }
}

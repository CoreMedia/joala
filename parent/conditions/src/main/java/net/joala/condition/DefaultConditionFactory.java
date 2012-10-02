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

import net.joala.condition.timing.TimeoutImpl;
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
  private final Timeout timeout;

  /**
   * <p>
   * Create factory for conditions with the given timeout behavior.
   * </p>
   *
   * @param timeout the timeout behavior (i. e. the default time to time out)
   */
  @Deprecated
  public DefaultConditionFactory(@Nonnull final net.joala.condition.timing.Timeout timeout) {
    this.timeout = ((TimeoutImpl)timeout).getWrapped();
  }

  /**
   * <p>
   * Create factory for conditions with the given timeout behavior.
   * </p>
   *
   * @param timeout the timeout behavior (i. e. the default time to time out)
   */
  public DefaultConditionFactory(@Nonnull final Timeout timeout) {
    this.timeout = timeout;
  }

  @Nonnull
  @Override
  public BooleanCondition booleanCondition(@Nonnull final Expression<Boolean> expression) {
    checkNotNull(expression, "Expression must not be null");
    return new DefaultBooleanCondition(expression, timeout);
  }

  @Nonnull
  @Override
  public <T> Condition<T> condition(@Nonnull final Expression<T> expression) {
    checkNotNull(expression, "Expression must not be null");
    return new DefaultCondition<T>(expression, timeout);
  }
}

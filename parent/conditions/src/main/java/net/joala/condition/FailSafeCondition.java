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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Condition which ensures that a runnable is executed after the condition
 * has been performed.
 *
 * @param <T> the type of the value which will be verified
 * @since 2/24/12
 */
public interface FailSafeCondition<T> extends Condition<T> {
  /**
   * Define runnable to execute after the condition has been performed.
   *
   * @param runnable runnable to execute; null to disable execution
   * @return self-reference
   */
  @Nonnull
  FailSafeCondition<T> runFinally(@Nullable Runnable runnable);

  /**
   * Define runnable to execute before the condition is being performed.
   *
   * @param runnable runnable to execute; null to disable execution
   * @return self-reference
   */
  @Nonnull
  FailSafeCondition<T> runBefore(@Nullable Runnable runnable);

  @Override
  @Nonnull
  FailSafeCondition<T> withMessage(@Nullable String message);

  @Override
  @Nonnull
  FailSafeCondition<T> withTimeoutFactor(@Nonnegative double factor);
}

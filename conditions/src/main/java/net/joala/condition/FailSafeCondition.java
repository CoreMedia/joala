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

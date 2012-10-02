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

import net.joala.time.Timeout;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * @since 2/26/12
 */
public class DefaultBooleanCondition extends DefaultCondition<Boolean> implements BooleanCondition {
  @Deprecated
  public DefaultBooleanCondition(@Nonnull final Expression<Boolean> expression, @Nonnull final net.joala.condition.timing.Timeout timeout) {
    super(expression, timeout);
  }

  public DefaultBooleanCondition(@Nonnull final Expression<Boolean> expression, @Nonnull final Timeout timeout) {
    super(expression, timeout);
  }

  @Override
  public final void assumeTrue() {
    assumeEquals(TRUE);
  }

  @Override
  public final void assumeFalse() {
    assumeEquals(FALSE);
  }

  @Override
  public final void assertTrue() {
    assertEquals(TRUE);
  }

  @Override
  public final void assertFalse() {
    assertEquals(FALSE);
  }

  @Override
  @Nonnull
  public DefaultBooleanCondition runFinally(@Nullable final Runnable runnable) {
    super.runFinally(runnable);
    return this;
  }

  @Override
  @Nonnull
  public DefaultBooleanCondition runBefore(@Nullable final Runnable runnable) {
    super.runBefore(runnable);
    return this;
  }

  @Override
  @Nonnull
  public DefaultBooleanCondition withMessage(@Nullable final String newMessage) {
    super.withMessage(newMessage);
    return this;
  }

  @Override
  @Nonnull
  public DefaultBooleanCondition withTimeoutFactor(@Nonnegative final double newFactor) {
    super.withTimeoutFactor(newFactor);
    return this;
  }
}

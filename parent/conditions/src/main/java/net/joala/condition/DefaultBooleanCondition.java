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

import net.joala.expression.Expression;
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
  public void waitUntilFalse() {
    waitUntilEquals(FALSE);
  }

  @Override
  public void waitUntilTrue() {
    waitUntilEquals(TRUE);
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

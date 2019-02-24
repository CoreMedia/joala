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
import edu.umd.cs.findbugs.annotations.Nullable;
import net.joala.expression.Expression;
import net.joala.time.Timeout;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * @since 2/26/12
 */
public class DefaultBooleanCondition extends DefaultCondition<Boolean> implements BooleanCondition {
  public DefaultBooleanCondition(@NonNull final Expression<Boolean> expression, @NonNull final Timeout timeout) {
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
  @NonNull
  public DefaultBooleanCondition runFinally(@Nullable final Runnable runnable) {
    super.runFinally(runnable);
    return this;
  }

  @Override
  @NonNull
  public DefaultBooleanCondition runBefore(@Nullable final Runnable runnable) {
    super.runBefore(runnable);
    return this;
  }

  @Override
  @NonNull
  public DefaultBooleanCondition withMessage(@Nullable final String newMessage) {
    super.withMessage(newMessage);
    return this;
  }

  @Override
  @NonNull
  public DefaultBooleanCondition withTimeoutFactor(final double newFactor) {
    super.withTimeoutFactor(newFactor);
    return this;
  }
}

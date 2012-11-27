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

package net.joala.condition.timing;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Implementation of {@link Timeout}.
 * </p>
 *
 * @since 8/22/12
 * @deprecated since 0.5.0; use {@link net.joala.time.TimeoutImpl} instead
 */
@Deprecated
public class TimeoutImpl implements Timeout {
  private final net.joala.time.Timeout wrapped;

  public TimeoutImpl(@Nonnegative final long amount, @Nonnull final TimeUnit unit) {
    this(new net.joala.time.TimeoutImpl(amount, unit));
  }

  public TimeoutImpl(@Nonnull final net.joala.time.Timeout wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  @Nonnegative
  @SuppressWarnings("PMD.ShortMethodName")
  public long in(@Nonnull final TimeUnit targetUnit) {
    return wrapped.in(targetUnit);
  }

  @Override
  @Nonnegative
  @SuppressWarnings("PMD.ShortMethodName")
  public long in(@Nonnull final TimeUnit targetUnit, @Nonnegative final double factor) {
    return wrapped.in(targetUnit, factor);
  }

  public net.joala.time.Timeout getWrapped() {
    return wrapped;
  }
}

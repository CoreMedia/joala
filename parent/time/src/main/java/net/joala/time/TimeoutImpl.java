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

package net.joala.time;

import com.google.common.base.Objects;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.round;

/**
 * <p>
 * Implementation of {@link Timeout}.
 * </p>
 *
 * @since 8/22/12
 */
public class TimeoutImpl implements Timeout {
  private final long amount;
  private final TimeUnit unit;

  public TimeoutImpl(@Nonnegative final long amount, @Nonnull final TimeUnit unit) {
    checkArgument(amount >= 0L, "amount must be positive: %s", amount);
    checkNotNull(unit, "time unit must not be null");
    this.amount = amount;
    this.unit = unit;
  }

  @Override
  @Nonnegative
  @SuppressWarnings("PMD.ShortMethodName")
  public long in(@Nonnull final TimeUnit targetUnit) {
    checkNotNull(targetUnit, "time unit must not be null");
    return targetUnit.convert(amount, unit);
  }

  @Override
  @Nonnegative
  @SuppressWarnings("PMD.ShortMethodName")
  public long in(@Nonnull final TimeUnit targetUnit, @Nonnegative final double factor) {
    checkArgument(Double.compare(factor, 0d) >= 0, "factor must be positive: %s", factor);
    return round(in(targetUnit) * factor);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("amount", amount)
            .add("unit", unit)
            .toString();
  }

}

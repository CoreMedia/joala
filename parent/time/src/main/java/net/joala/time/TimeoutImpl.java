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
    checkArgument(amount >= 0l, "amount must be positive: %s", amount);
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

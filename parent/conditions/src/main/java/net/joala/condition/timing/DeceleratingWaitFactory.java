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

import com.google.common.base.Objects;
import net.joala.time.Timeout;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * Creates instances of {@link DeceleratingWait}.
 * </p>
 *
 * @since 0.8.0
 */
public class DeceleratingWaitFactory implements WaitFactory {
  /**
   * Base timeout to apply.
   */
  @Nonnull
  private final Timeout timeout;

  /**
   * <p>
   * Factory with the given base timeout to apply to all wait strategies.
   * </p>
   *
   * @param timeout base timeout to apply
   */
  public DeceleratingWaitFactory(@Nonnull final Timeout timeout) {
    checkNotNull(timeout, "Timeout must not be null.");
    this.timeout = timeout;
  }

  @Nonnull
  @Override
  public Wait get(@Nonnegative final double tolerance, @Nonnull final WaitFailStrategy failStrategy) {
    return new DeceleratingWait(timeout, tolerance, failStrategy);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("timeout", timeout)
            .toString();
  }
}

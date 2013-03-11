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

package net.joala.condition.timing;

import javax.annotation.Nullable;

/**
 * <p>
 * Exception which signals that a condition is not fulfilled within time.
 * </p>
 *
 * @since 8/23/12
 */
public final class WaitTimeoutException extends RuntimeException {
  public WaitTimeoutException(@Nullable final String message, @Nullable final Throwable cause) {
    super(message, cause);
  }
}

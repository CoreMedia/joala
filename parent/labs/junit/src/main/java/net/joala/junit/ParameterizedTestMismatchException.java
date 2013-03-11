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

package net.joala.junit;

/**
 * @since 10/8/12
 */
public class ParameterizedTestMismatchException extends RuntimeException {
  public ParameterizedTestMismatchException() {
  }

  public ParameterizedTestMismatchException(final Throwable cause) {
    super(cause);
  }

  public ParameterizedTestMismatchException(final String message) {
    super(message);
  }

  public ParameterizedTestMismatchException(final String message, final Throwable cause) {
    super(message, cause);
  }
}

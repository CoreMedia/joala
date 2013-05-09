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

package net.joala.internal.data;

/**
 * <p>
 * Exception thrown when creating data for tests failed for any reason. Reasons might be misconfiguration
 * of the random generator or runtime failures like random files which cannot be created.
 * </p>
 *
 * @since 9/16/12
 */
public class DataProvidingException extends Exception {
  /**
   * Exception without cause and message. Discouraged to use. Please assist your testers to provide meaningful
   * exceptions for easier debugging.
   */
  public DataProvidingException() {
  }

  /**
   * Exception with a given cause.
   *
   * @param cause causing exception
   */
  public DataProvidingException(final Throwable cause) {
    super(cause);
  }

  /**
   * Exception with a given message.
   *
   * @param message message
   */
  public DataProvidingException(final String message) {
    super(message);
  }

  /**
   * Exception with given cause and message.
   *
   * @param message message
   * @param cause   causing exception
   */
  public DataProvidingException(final String message, final Throwable cause) {
    super(message, cause);
  }
}

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

package net.joala.expression;

/**
 * This exception is thrown by {@link Expression#get()} when the
 * expression cannot (yet) be computed and contains a root cause.
 */
public class ExpressionEvaluationException extends RuntimeException {

  /**
   * Exception without message and cause.
   */
  public ExpressionEvaluationException() {
    // nothing to do but make PMD happy
  }

  /**
   * Exception with given message.
   *
   * @param message message
   */
  public ExpressionEvaluationException(final String message) {
    super(message);
  }

  /**
   * Exception with message and cause.
   *
   * @param message message
   * @param cause   cause
   */
  public ExpressionEvaluationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * Exception with given cause.
   *
   * @param cause cause
   */
  public ExpressionEvaluationException(final Throwable cause) {
    super(cause);
  }

}

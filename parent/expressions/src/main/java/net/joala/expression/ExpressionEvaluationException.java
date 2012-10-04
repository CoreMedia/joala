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

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

package net.joala.newdata;

/**
 * <p>
 * Exception thrown when creating data for tests failed for any reason. Reasons might be misconfiguration
 * of the random generator or runtime failures like random files which cannot be created.
 * </p>
 *
 * @since 9/16/12
 */
public class DataProvidingException extends RuntimeException {
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

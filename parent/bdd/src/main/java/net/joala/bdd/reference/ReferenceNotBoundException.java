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

package net.joala.bdd.reference;

import javax.annotation.Nullable;

/**
 * <p>
 * Denotes that you try to read a reference which is not yet bound to a value.
 * </p>
 *
 * @since 6/5/12
 */
public class ReferenceNotBoundException extends RuntimeException {

  /**
   * <p>
   * Constructor setting a message.
   * </p>
   *
   * @param message a failure message
   */
  public ReferenceNotBoundException(@Nullable final String message) {
    super(message);
  }

}

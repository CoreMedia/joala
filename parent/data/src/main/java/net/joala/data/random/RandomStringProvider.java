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

package net.joala.data.random;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p>
 * Provides random strings, typically of some minimum and maximum length and with specified
 * types of characters contained.
 * </p>
 *
 * @since 9/17/12
 */
public interface RandomStringProvider extends RandomDataProvider<String> {
  /**
   * <p>
   * Specify the exact length of the strings to return. Sets maximum and minimum length
   * to the same value.
   * </p>
   *
   * @param len the length of the strings to create
   * @return self-reference
   */
  @Nonnull
  RandomStringProvider length(@Nonnegative int len);

  /**
   * <p>
   * Specify the maximum length of the strings to return.
   * </p>
   *
   * @param len maximum length of strings
   * @return self-reference
   */
  @Nonnull
  RandomStringProvider maxLength(@Nonnegative int len);

  /**
   * <p>
   * Specify the minimum length of the strings to return.
   * </p>
   *
   * @param len minimum length of strings
   * @return self-reference
   */
  @Nonnull
  RandomStringProvider minLength(@Nonnegative int len);

  /**
   * <p>
   * Specify the type of characters to create.
   * </p>
   *
   * @param type character type
   * @return self-reference
   */
  @Nonnull
  RandomStringProvider type(@Nonnull RandomStringType type);

  /**
   * <p>
   * Specify a prefix to put in front of the random string. This is sometimes useful
   * in order to assist assertions to be easy to interpret. The prefix length is subtracted from
   * the total valid length of the generated string. So it must not exceed the maximum length.
   * </p>
   *
   * @param prefix prefix for the string; {@code null} or empty string for no prefix
   * @return self-reference
   */
  @Nonnull
  RandomStringProvider prefix(@Nullable String prefix);

  /**
   * <p>
   * Specify a postfix to attach to the random string. This is sometimes useful
   * in order to assist assertions to be easy to interpret. The postfix length is subtracted from
   * the total valid length of the generated string. So it must not exceed the maximum length.
   * </p>
   *
   * @param postfix postfix for the string; {@code null} or empty string for no postfix
   * @return self-reference
   */
  @Nonnull
  RandomStringProvider postfix(@Nullable String postfix);
}

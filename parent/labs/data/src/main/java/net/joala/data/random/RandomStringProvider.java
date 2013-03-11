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

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

package net.joala.internal.data.random;

import com.google.common.base.Objects;
import net.joala.internal.data.DataProvidingException;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static net.joala.internal.data.random.RandomStringType.ALL;

/**
 * @since 9/17/12
 */
public class DefaultRandomStringProvider extends AbstractRandomDataProvider<String> implements RandomStringProvider {
  private static final int DEFAULT_MIN_LEN = 1;
  private static final int DEFAULT_MAX_LEN = 16;
  private static final RandomStringType DEFAULT_STRING_TYPE = ALL;
  @Nonnegative
  private int minLen;
  @Nonnegative
  private int maxLen;
  @Nonnull
  private RandomStringType stringType;
  @Nullable
  private String prefix;
  @Nullable
  private String postfix;

  public DefaultRandomStringProvider() {
    this(DEFAULT_STRING_TYPE, DEFAULT_MIN_LEN, DEFAULT_MAX_LEN);
  }

  public DefaultRandomStringProvider(@Nonnegative final int defaultLen) {
    this(DEFAULT_STRING_TYPE, defaultLen, defaultLen);
  }

  public DefaultRandomStringProvider(@Nonnegative final int defaultMinLen, @Nonnegative final int defaultMaxLen) {
    this(DEFAULT_STRING_TYPE, defaultMinLen, defaultMaxLen);
  }

  public DefaultRandomStringProvider(@Nonnull final RandomStringType type) {
    this(type, DEFAULT_MIN_LEN, DEFAULT_MAX_LEN);
  }

  public DefaultRandomStringProvider(@Nonnull final RandomStringType type, @Nonnegative final int defaultLen) {
    this(type, defaultLen, defaultLen);
  }

  public DefaultRandomStringProvider(@Nonnull final RandomStringType type, @Nonnegative final int defaultMinLen, @Nonnegative final int defaultMaxLen) {
    checkArgument(defaultMinLen <= defaultMaxLen, "Min length must not be greater than max len: %s >= %s", defaultMinLen, defaultMaxLen);
    checkArgument(defaultMinLen >= 0, "Minimum length must be non-negative.");
    checkArgument(defaultMaxLen >= 0, "Maximum length must be non-negative.");
    checkNotNull(type, "String Type must not be null.");
    this.maxLen = defaultMaxLen;
    this.minLen = defaultMinLen;
    this.stringType = type;
  }

  @Override
  public String get() throws DataProvidingException {
    return stringType.get(getLength());
  }

  @Nonnegative
  protected final int getLength() throws DataProvidingException {
    final int prefixLength = prefix == null ? 0 : prefix.length();
    final int postfixLength = postfix == null ? 0 : postfix.length();
    final int predefinedLength = prefixLength + postfixLength;

    checkState(maxLen >= predefinedLength, "Maximum length (%s) is less than pre- and/or postfix (%s).", maxLen, predefinedLength);
    final int adjustedMaxLen = maxLen - predefinedLength;
    if (minLen == maxLen) {
      return adjustedMaxLen;
    } else {
      final int adjustedMinLen;
      if (minLen < predefinedLength) {
        adjustedMinLen = 0;
      } else {
        adjustedMinLen = minLen - predefinedLength;
      }
      return new RandomIntegerProvider().min(adjustedMinLen).max(adjustedMaxLen).get();
    }
  }

  @Nonnull
  protected final RandomStringType getStringType() {
    return stringType;
  }

  @Nullable
  protected final String getPostfix() {
    return postfix;
  }

  @Nullable
  protected final String getPrefix() {
    return prefix;
  }

  @Override
  @Nonnull
  public final RandomStringProvider type(@Nonnull final RandomStringType type) {
    stringType = type;
    return this;
  }

  @Override
  @Nonnull
  public final RandomStringProvider length(@Nonnegative final int len) {
    maxLen = len;
    minLen = len;
    return this;
  }

  @Override
  @Nonnull
  public final RandomStringProvider maxLength(@Nonnegative final int len) {
    maxLen = len;
    return this;
  }

  @Override
  @Nonnull
  public final RandomStringProvider minLength(@Nonnegative final int len) {
    minLen = len;
    return this;
  }

  @Nonnull
  @Override
  public RandomStringProvider postfix(@Nullable final String postfix) {
    this.postfix = postfix;
    return this;
  }

  @Nonnull
  @Override
  public RandomStringProvider prefix(@Nullable final String prefix) {
    this.prefix = prefix;
    return this;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("minLen", minLen)
            .add("maxLen", maxLen)
            .add("stringType", stringType)
            .toString();
  }
}

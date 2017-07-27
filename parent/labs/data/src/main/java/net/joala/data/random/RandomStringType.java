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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.RandomStringUtils;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.RandomStringUtils.random;

/**
 * <p>
 * Types of characters to support in random string generation. The parameters for the random string type
 * are derived from {@link RandomStringUtils}.
 * </p>
 *
 * @deprecated Will be removed soon.
 */
@SuppressWarnings("deprecation")
@Deprecated
public enum RandomStringType {
  /**
   * Use all available characters.
   *
   * @see RandomStringUtils#random(int)
   */
  ALL(0, 0, false, false),
  /**
   * Use ASCII characters only.
   *
   * @see RandomStringUtils#randomAscii(int)
   */
  ASCII(32, 127, false, false),
  /**
   * Use alphabetic characters only.
   *
   * @see RandomStringUtils#randomAlphabetic(int)
   */
  ALPHABETIC(0, 0, true, false),
  /**
   * Use alphabetic and numeric characters only.
   *
   * @see RandomStringUtils#randomAlphanumeric(int)
   */
  ALPHANUMERIC(0, 0, true, true),
  /**
   * Use numeric characters only.
   *
   * @see RandomStringUtils#randomNumeric(int)
   */
  NUMERIC(0, 0, false, true);
  /**
   * Start value for characters.
   */
  private final int startChr;
  /**
   * End value for characters
   */
  private final int endChr;
  /**
   * If {@code true} will contain alphabetic characters.
   */
  private final boolean containsAlphabetic;
  /**
   * If {@code true} will contain numeric characters.
   */
  private final boolean containsNumbers;

  /**
   * <p>
   * Constructor providing parameters for the random string type.
   * </p>
   *
   * @param startChr           start value for characters
   * @param endChr             end value for characters
   * @param containsAlphabetic if {@code true} will contain alphabetic characters
   * @param containsNumbers    if {@code true} will contain numeric characters
   */
  RandomStringType(final int startChr, final int endChr, final boolean containsAlphabetic, final boolean containsNumbers) {
    this.startChr = startChr;
    this.endChr = endChr;
    this.containsAlphabetic = containsAlphabetic;
    this.containsNumbers = containsNumbers;
  }

  /**
   * <p>
   * Retrieve a random string of the given length.
   * </p>
   *
   * @param count number of characters
   * @return random string of the given length
   */
  @Nonnull
  public String get(final int count) {
    return random(count, startChr, endChr, containsAlphabetic, containsNumbers);
  }

  @VisibleForTesting
  int getEndChr() {
    return endChr;
  }

  @VisibleForTesting
  int getStartChr() {
    return startChr;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("startChr", startChr)
            .add("endChr", endChr)
            .add("containsAlphabetic", containsAlphabetic)
            .add("containsNumbers", containsNumbers)
            .toString();
  }
}

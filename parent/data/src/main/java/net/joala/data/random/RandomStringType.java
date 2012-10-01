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

import com.google.common.base.Objects;
import org.apache.commons.lang3.RandomStringUtils;

import static org.apache.commons.lang3.RandomStringUtils.random;

/**
 * <p>
 * Types of characters to support in random string generation. The parameters for the random string type
 * are derived from {@link RandomStringUtils}.
 * </p>
 *
 * @since 9/17/12
 */
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
  public String get(final int count) {
    return random(count, startChr, endChr, containsAlphabetic, containsNumbers);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("startChr", startChr)
            .add("endChr", endChr)
            .add("onlyAlphabetic", containsAlphabetic)
            .add("onlyNumbers", containsNumbers)
            .toString();
  }
}

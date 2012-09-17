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

import static org.apache.commons.lang3.RandomStringUtils.random;

/**
 * @since 9/17/12
 */
public enum RandomStringType {
  ALL(0, 0, false, false),
  ASCII(32, 127, false, false),
  ALPHABETIC(0, 0, true, false),
  ALPHANUMERIC(0, 0, true, true),
  NUMERIC(0, 0, false, true);
  private final int startChr;
  private final int endChr;
  private final boolean onlyAlphabetic;
  private final boolean onlyNumbers;

  RandomStringType(final int startChr, final int endChr, final boolean onlyAlphabetic, final boolean onlyNumbers) {
    this.startChr = startChr;
    this.endChr = endChr;
    this.onlyAlphabetic = onlyAlphabetic;
    this.onlyNumbers = onlyNumbers;
  }

  public String get(final int count) {
    return random(count, startChr, endChr, onlyAlphabetic, onlyNumbers);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("startChr", startChr)
            .add("endChr", endChr)
            .add("onlyAlphabetic", onlyAlphabetic)
            .add("onlyNumbers", onlyNumbers)
            .toString();
  }
}

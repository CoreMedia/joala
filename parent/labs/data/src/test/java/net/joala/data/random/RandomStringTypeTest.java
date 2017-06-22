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

import com.google.common.base.MoreObjects;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static net.joala.junit.ParameterizedParametersBuilders.singletonParametersBuilder;
import static net.joala.testlet.ToStringTestlet.toStringTestlet;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

/**
 * <p>
 * Tests {@link RandomStringType}.
 * </p>
 *
 * @since 10/8/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
@RunWith(Parameterized.class)
public class RandomStringTypeTest {
  private static final int GREAT_LENGTH = 1024;
  private final RandomStringType type;

  public RandomStringTypeTest(final RandomStringType type) {
    this.type = type;
  }

  @Test
  public void min_max_in_correct_order() throws Exception {
    final int startChr = type.getStartChr();
    final int endChr = type.getEndChr();
    assertThat("end >= start", endChr, greaterThanOrEqualTo(startChr));
  }

  @Test
  public void created_chars_should_be_within_boundaries() throws Exception {
    final int startChr = type.getStartChr();
    final int endChr = type.getEndChr();
    if (startChr == 0 && endChr == 0) {
      // skip, no need to test
      return;
    }
    assumeThat(endChr, greaterThanOrEqualTo(startChr));
    final int rangeChr = endChr - startChr;
    // Could also test just one character - but this way we have some more characters to test.
    final String str = type.get(rangeChr);
    for (int i = 0; i < str.length(); i++) {
      final char chr = str.charAt(i);
      final Matcher<Character> upperBoundMatcher = lessThanOrEqualTo((char) endChr);
      final Matcher<Character> lowerBoundMatcher = greaterThanOrEqualTo((char) startChr);
      assertThat("Character should be within type limits",
              chr, allOf(lowerBoundMatcher, upperBoundMatcher));
    }
  }

  @Test
  public void creation_with_length_0_should_create_string_with_length_0() throws Exception {
    assertThat(type.get(0), equalTo(""));
  }

  @Test
  public void creation_with_length_1_should_create_string_with_length_1() throws Exception {
    assertThat(type.get(1).length(), equalTo(1));
  }

  @Test
  public void creation_with_great_length_should_create_string_with_great_length() throws Exception {
    assertThat(type.get(GREAT_LENGTH).length(), equalTo(GREAT_LENGTH));
  }

  @Test(expected = IllegalArgumentException.class)
  public void deny_to_create_string_with_negative_length() throws Exception {
    type.get(-1);
  }

  @Test
  public void toString_should_meet_requirements() throws Throwable {
    toStringTestlet(type).run();
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return singletonParametersBuilder(RandomStringTypeTest.class).add(RandomStringType.values()).build();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("type", type)
            .toString();
  }
}

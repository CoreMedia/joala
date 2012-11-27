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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.matcher.text;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;

/**
 * @since 9/19/12
 */
public class MatchesPattern extends TypeSafeMatcher<CharSequence> {
  private final Pattern pattern;

  public MatchesPattern(final Pattern pattern) {
    this.pattern = pattern;
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("a sequence of characters matching pattern ");
    description.appendValue(pattern);
  }

  @Override
  protected boolean matchesSafely(final CharSequence item) {
    return pattern.matcher(item).matches();
  }

  @Factory
  public static Matcher<CharSequence> matchesPattern(final Pattern pattern) {
    return new MatchesPattern(pattern);
  }

  @Factory
  public static Matcher<CharSequence> matchesPattern(final String pattern) {
    return new MatchesPattern(Pattern.compile(pattern));
  }
}

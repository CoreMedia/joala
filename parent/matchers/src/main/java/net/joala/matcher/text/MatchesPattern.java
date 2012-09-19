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

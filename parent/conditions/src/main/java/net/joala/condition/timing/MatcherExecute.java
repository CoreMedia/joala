/*
 * Copyright 2013 CoreMedia AG
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

package net.joala.condition.timing;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

/**
 * Utility class to format matcher descriptions.
 *
 * @since 2013-05-08
 */
public final class MatcherExecute {
  private MatcherExecute() {
  }

  public static <T> void match(final String reason, final T actual, final Matcher<? super T> matcher, final FailStrategy failStrategy) {
    if (!matcher.matches(actual)) {
        final Description description = new StringDescription();
        description.appendText(reason)
                   .appendText("\nExpected: ")
                   .appendDescriptionOf(matcher)
                   .appendText("\n     but: ");
        matcher.describeMismatch(actual, description);

        failStrategy.fail(description.toString());
    }
  }

  public interface FailStrategy {
    void fail(String message);
  }

  public static final class AssertionErrorStrategy implements FailStrategy {
    @Override
    public void fail(final String message) {
      throw new AssertionError(message);
    }
  }
}

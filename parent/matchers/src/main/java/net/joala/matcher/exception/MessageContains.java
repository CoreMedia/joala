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

package net.joala.matcher.exception;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;

import static java.lang.String.format;
import static org.hamcrest.core.StringContains.containsString;

/**
 * @since 8/28/12
 */
public class MessageContains extends CustomTypeSafeMatcher<Throwable> {
  private final String containedMessage;
  private final boolean recurseCauses;

  public MessageContains(final String containedMessage) {
    this(containedMessage, false);
  }

  public MessageContains(final String containedMessage, final boolean recurseCauses) {
    super(format("message contains '%s'", containedMessage));
    this.containedMessage = containedMessage;
    this.recurseCauses = recurseCauses;
  }

  @Override
  protected boolean matchesSafely(final Throwable item) {
    return containsString(containedMessage).matches(item.getMessage())
            || (recurseCauses && item.getCause() != null && matchesSafely(item.getCause()));
  }

  public static Matcher<Throwable> messageContains(final String expectedMessage, final boolean recurseCauses) {
    return new MessageContains(expectedMessage, recurseCauses);
  }

  public static Matcher<Throwable> messageContains(final String expectedMessage) {
    return new MessageContains(expectedMessage);
  }
}

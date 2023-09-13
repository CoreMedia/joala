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

import javax.annotation.Nonnull;

/**
 * <p>
 * Matcher to search stacktrace of exception if it get caused by a given exception somewhere.
 * </p>
 *
 * @since 8/28/12
 */
public class CausedBy extends CustomTypeSafeMatcher<Throwable> {
  private final Throwable cause;

  public CausedBy(@Nonnull final Throwable cause) {
    super("is caused by");
    this.cause = cause;
  }

  @Override
  protected boolean matchesSafely(final Throwable item) {
    return item.getCause() != null && (item.getCause() == cause || matchesSafely(item.getCause()));
  }

  public static Matcher<Throwable> causedBy(final Throwable expected) {
    return new CausedBy(expected);
  }

}

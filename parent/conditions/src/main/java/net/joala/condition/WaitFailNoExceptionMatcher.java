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

package net.joala.condition;

import net.joala.matcher.DescriptionUtil;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @since 8/27/12
 */
final class WaitFailNoExceptionMatcher extends CustomTypeSafeMatcher<Throwable> {
  private final Object function;

  WaitFailNoExceptionMatcher(@Nonnull final Object function) {
    super("evaluation without exception");
    this.function = function;
  }

  @Override
  protected boolean matchesSafely(@Nullable final Throwable item) {
    return item == null;
  }

  @Override
  protected void describeMismatchSafely(@Nonnull final Throwable item,
                                        @Nonnull final Description mismatchDescription) {
    final StringWriter out = new StringWriter();
    item.printStackTrace(new PrintWriter(out));
    mismatchDescription.appendText("failed to evaluate: ");
    DescriptionUtil.describeTo(mismatchDescription, function);
    mismatchDescription.appendText(" because of: ");
    mismatchDescription.appendText(out.toString());
  }
}

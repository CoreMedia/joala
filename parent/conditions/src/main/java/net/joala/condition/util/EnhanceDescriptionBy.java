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

package net.joala.condition.util;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.core.DescribedAs;

/**
 * <p>
 * Use this wrapper for matchers to enhance it by additional
 * description. Can be used for example for assumption failures
 * where with current API it is not possible to add a message.
 * </p>
 *
 * @see <a href="https://github.com/KentBeck/junit/pull/489">Improve Assume to allow custom message</a>
 * @since 8/28/12
 */
public class EnhanceDescriptionBy<T> extends DescribedAs<T> {

  private final Matcher<T> matcher;

  public EnhanceDescriptionBy(
          @NonNull final String descriptionTemplate,
          @NonNull final Matcher<T> matcher,
          final Object... values) {
    super(descriptionTemplate, matcher, values);
    this.matcher = matcher;
  }

  @Override
  public void describeTo(@NonNull final Description description) {
    matcher.describeTo(description);
    description.appendText(" (");
    super.describeTo(description);
    description.appendText(")");
  }

  @SuppressWarnings("ParameterHidesMemberVariable")
  @Factory
  public static <T> Matcher<T> enhanceDescriptionBy(
          @NonNull final String descriptionTemplate,
          @NonNull final Matcher<T> matcher,
          final Object... values) {
    return new EnhanceDescriptionBy<>(descriptionTemplate, matcher, values);
  }

}

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

package net.joala.matcher.reflect;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import java.lang.reflect.AccessibleObject;

/**
 * <p>
 * Verifies if a given object is accessible (a field, method or such).
 * </p>
 *
 * @param <T> accessible object type
 * @since 10/9/12
 */
public class IsAccessible<T extends AccessibleObject> extends CustomTypeSafeMatcher<T> {
  /**
   * Constructor.
   */
  public IsAccessible() {
    super("is accessible");
  }

  @Override
  protected boolean matchesSafely(final T item) {
    return item.isAccessible();
  }

  /**
   * Create matcher which verifies if the given object is accessible.
   *
   * @param <T> accessible object type
   * @return matcher
   */
  @Factory
  public static <T extends AccessibleObject> Matcher<T> isAccessible() {
    return new IsAccessible<T>();
  }
}

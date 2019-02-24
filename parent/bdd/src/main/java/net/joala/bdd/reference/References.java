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

package net.joala.bdd.reference;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * <p>
 * Factory methods for creating references. These factory methods are meant to provide
 * a shortcut to create new instances.
 * </p>
 *
 * @see net.joala.bdd
 * @since 9/27/12
 */
public final class References {
  /**
   * Utility class - don't instantiate.
   */
  private References() {
  }

  /**
   * <p>
   * Factory method for simple (unlogged) references.
   * </p>
   * <dl>
   * <dt><strong>Usage:</strong></dt>
   * <dd><pre>{@code
   * Reference<String> myString = References.ref();
   * }</pre></dd>
   * </dl>
   *
   * @param <T> the type of value contained in the reference
   * @return a reference
   */
  @NonNull
  public static <T> Reference<T> ref() {
    return new ReferenceImpl<T>();
  }

  /**
   * <p>
   * Create a reference which is self-describing. This is especially meant to be
   * used together with steps logging. If you want to have silent (not logged)
   * references use {@link #ref()} instead.
   * </p>
   * <dl>
   * <dt><strong>Usage:</strong></dt>
   * <dd><pre>{@code
   * Reference<String> myString = References.ref("aName");
   * }</pre></dd>
   * </dl>
   *
   * @param name the name (or description) of the reference used for logging
   * @param <T>  the type of value contained in the reference
   * @return a reference
   * @see #ref()
   */
  @NonNull
  public static <T> Reference<T> ref(final String name) {
    return new SelfDescribingReferenceImpl<T>(name);
  }
}

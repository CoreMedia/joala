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

package net.joala.core.reflection;

import com.google.common.base.Objects;

import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedAction;

/**
 * Privileged action to set something to accessible.
 * @param <T> the type of the accessible object
 * @since 10/8/12
 */
public class SetAccessibleAction<T extends AccessibleObject> implements PrivilegedAction<Void> {
  private final T object;

  /**
   * Creates privileged action bound to the given accessible object.
   * @param object object to make accessible
   */
  public SetAccessibleAction(final T object) {
    this.object = object;
  }

  @Override
  public Void run() {
    object.setAccessible(true);
    return null;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("object", object)
            .toString();
  }
}

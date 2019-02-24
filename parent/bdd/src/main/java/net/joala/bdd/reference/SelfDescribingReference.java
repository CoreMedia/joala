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

import edu.umd.cs.findbugs.annotations.Nullable;
import org.hamcrest.SelfDescribing;

/**
 * <p>
 * Self describable references may be printed to log along with the steps where they are used.
 * </p>
 *
 * @since 6/21/12
 */
public interface SelfDescribingReference<T> extends SelfDescribing, Reference<T> {
  /**
   * Retrieve the name of this reference.
   * @return name
   */
  @Nullable
  String getName();
}

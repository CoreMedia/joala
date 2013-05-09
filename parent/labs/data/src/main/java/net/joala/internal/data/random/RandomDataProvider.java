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

package net.joala.internal.data.random;

import net.joala.internal.data.DataProvider;

import javax.annotation.Nonnull;

/**
 * <p>
 * Provides random data. Random generators will provide configuration methods which
 * again return self-references to build up chained-call configuration.
 * </p>
 *
 * @since 9/17/12
 */
public interface RandomDataProvider<T> extends DataProvider<T> {
  /**
   * <p>
   * Disbands any (obvious) configuration options. Implementations
   * might decide if the resulting object is immutable or not.
   * </p>
   *
   * @return a random data provider without any more configuration API
   */
  // returning only the DataProvider here also prohibits multiple calls to fixate().
  @Nonnull
  DataProvider<T> fixate();
}

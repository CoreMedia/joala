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
 * Abstract implementation of a random data provider.
 * </p>
 *
 * @since 9/17/12
 */
public abstract class AbstractRandomDataProvider<T> implements RandomDataProvider<T> {
  @Override
  @Nonnull
  public final DataProvider<T> fixate() {
    return this;
  }

}

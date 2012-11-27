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

package net.joala.newdata.random;

import net.joala.data.random.RandomDataProvider;
import net.joala.newdata.DataProvider;

import javax.annotation.Nullable;

/**
 * <p>
 * Provides random numbers. Configuration methods always return self-references.
 * </p>
 *
 * @since 9/17/12
 */
public interface RandomNumberProvider<T extends Number> extends DataProvider<T> {
  @Override
  T get();
}

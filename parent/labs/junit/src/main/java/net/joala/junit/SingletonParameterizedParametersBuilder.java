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

package net.joala.junit;

/**
 * <p>
 * Builder for parameters assuming that all parameters are singletons. Thus each single
 * object added will cause a test-run.
 * </p>
 *
 * @since 10/8/12
 */
public class SingletonParameterizedParametersBuilder extends DefaultParameterizedParametersBuilder {
  public SingletonParameterizedParametersBuilder(final Class<?> testClass) {
    super(testClass);
  }

  @Override
  public ParameterizedParametersBuilder add(final Object... objects) {
    for (final Object object : objects) {
      super.add(object);
    }
    return this;
  }
}

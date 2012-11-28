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

import java.util.Collection;

/**
 * <p>
 * Builder for parameters for running parametrized JUnit tests.
 * </p>
 *
 * @since 10/8/12
 */
public interface ParameterizedParametersBuilder {
  /**
   * <p>
   * Add objects for the one test run.
   * </p>
   *
   * @param objects objects to add, each must match to one constructor argument
   * @return self-reference
   */
  ParameterizedParametersBuilder add(Object... objects);

  /**
   * Build the data which then should be returned by the methood annotated
   * with {@code &#64;Parameterized.Parameters}.
   *
   * @return parametrized test data
   */
  Collection<Object[]> build();
}

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

package net.joala.testlet;

/**
 * <p>
 *   A testlet is a set of tests to run. Typically one set of tests is responsible for one
 *   small feature you want to test, like if the {@code toString()} method meets your requirements and such.
 * </p>
 * @since 10/9/12
 */
public interface Testlet {
  /**
   * <p>
   * Runs the testlet. This trigger a group of tests with the provided artifact(s).
   * </p>
   * @throws Throwable in case of an error; most likely a single assertion error or multiple failures
   * bundled in one exception; all of them recognized by standard JUnit executors.
   */
  @SuppressWarnings("ProhibitedExceptionDeclared")
  void run() throws Throwable; // NOSONAR: exception Throwable inherited from JUnit API
}

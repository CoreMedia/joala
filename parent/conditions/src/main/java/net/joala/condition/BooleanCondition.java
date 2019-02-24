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

package net.joala.condition;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Specialized condition for boolean states (true/false). Offers convenience
 * methods like {@link #assumeTrue()}.
 *
 * @since 2/27/12
 */
public interface BooleanCondition extends Condition<Boolean> {
  /**
   * Convenience method to assume the condition to become true.
   */
  void assumeTrue();

  /**
   * Convenience method to assume the condition to become false.
   */
  void assumeFalse();

  /**
   * Convenience method to assert the condition to become true.
   */
  void assertTrue();

  /**
   * Convenience method to assert the condition to become false.
   */
  void assertFalse();

  /**
   * Convenience method to wait until the condition becomes true.
   */
  void waitUntilTrue();

  /**
   * Convenience method to wait until the condition becomes false.
   */
  void waitUntilFalse();

  @Override
  @NonNull
  BooleanCondition withMessage(@Nullable String message);

  @Override
  @NonNull
  BooleanCondition withTimeoutFactor(double factor);
}

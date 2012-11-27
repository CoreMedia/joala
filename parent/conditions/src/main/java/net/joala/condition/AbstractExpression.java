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

package net.joala.condition;

import org.hamcrest.Description;

import javax.annotation.Nullable;

/**
 * <p>
 * Convenience abstract implementation of {@link Expression} which has an empty
 * implementation of {@link #describeTo(Description)}.
 * </p>
 *
 * @param <T> the result type of the expression
 * @since 2/27/12
 * @deprecated since 0.5.0; use {@code net.joala.expression.AbstractExpression} instead.
 */
@Deprecated
public abstract class AbstractExpression<T> extends net.joala.expression.AbstractExpression<T> {
  /**
   * <p>
   * Constructor providing an empty description.
   * </p>
   */
  @SuppressWarnings("UnusedDeclaration")
  protected AbstractExpression() {
    this(null);
  }

  /**
   * <p>
   * Constructor providing a plain text description of this expression for debugging purpose.
   * </p>
   *
   * @param simpleDescription the simple description
   */
  protected AbstractExpression(@Nullable final String simpleDescription) {
    super(simpleDescription);
  }
}

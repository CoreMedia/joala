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

/**
 * An expression is handed over to conditions to be evaluated again and again.
 * To implement it is recommended to extend {@link AbstractExpression} which ensures
 * to stay compatible with future changes and for convenience provides an empty implementation
 * of {@link #describeTo(Description)}.
 *
 * @param <T> the result type of the expression
 * @since 2/27/12
 * @deprecated since 0.5.0; use {@code net.joala.expressions.Expression} instead.
 */
@Deprecated
public interface Expression<T> extends net.joala.expression.Expression {
}

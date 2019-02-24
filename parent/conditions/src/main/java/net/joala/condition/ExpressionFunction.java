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

import com.google.common.base.Function;
import edu.umd.cs.findbugs.annotations.Nullable;
import net.joala.condition.timing.IgnorableStateQueryException;
import net.joala.expression.Expression;
import net.joala.expression.ExpressionEvaluationException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * A function to evaluate an expression. It especially wraps exceptions of type
 * {@link ExpressionEvaluationException} into {@link IgnorableStateQueryException}.
 * </p>
 *
 * @param <T> the type of the value the expression returns
 * @since 9/18/12
 */
final class ExpressionFunction<T> implements Function<Expression<T>, T> {
  /**
   * <p>
   * Evaluates expression and makes any {@link ExpressionEvaluationException} ignorable.
   * </p>
   *
   * @param input the expression to evaluate
   * @return the result of the expression
   */
  @Override
  @Nullable
  public T apply(final Expression<T> input) {
    checkNotNull(input, "Expression must not be null.");
    try {
      return input.get();
    } catch (ExpressionEvaluationException e) {
      throw new IgnorableStateQueryException(this, e);
    }
  }
}

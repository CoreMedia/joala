package net.joala.condition;

import org.hamcrest.SelfDescribing;

import javax.annotation.Nullable;

/**
 * An expression is handed over to conditions to be evaluated again and again.
 * To implement it is recommended to extend {@link AbstractExpression} which ensures
 * to stay compatible with future changes and for convenience provides an empty implementation
 * of {@link #describeTo(org.hamcrest.Description)}.
 *
 * @param <T> the result type of the expression
 * @since 2/27/12
 */
public interface Expression<T> extends SelfDescribing {
  /**
   * Retrieve the result of the expression.
   *
   * @return expression result
   * @throws ExpressionEvaluationException when the expression cannot (yet) be computed, but this failure is "volatile", i.e.
   *                                       it is expected to vanish later, so it makes sense to re-evaluate the expression
   */
  @Nullable
  T get();
}

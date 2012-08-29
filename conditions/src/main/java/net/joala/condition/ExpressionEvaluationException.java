package net.joala.condition;

/**
 * This exception is thrown by {@link Expression#get()} when the
 * expression cannot (yet) be computed and contains a root cause.
 */
public class ExpressionEvaluationException extends RuntimeException {

  public ExpressionEvaluationException() {
  }

  public ExpressionEvaluationException(final String message) {
    super(message);
  }

  public ExpressionEvaluationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ExpressionEvaluationException(final Throwable cause) {
    super(cause);
  }

}

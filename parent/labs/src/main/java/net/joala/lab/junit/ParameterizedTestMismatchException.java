package net.joala.lab.junit;

/**
 * @since 10/8/12
 */
public class ParameterizedTestMismatchException extends RuntimeException {
  public ParameterizedTestMismatchException() {
  }

  public ParameterizedTestMismatchException(final Throwable cause) {
    super(cause);
  }

  public ParameterizedTestMismatchException(final String message) {
    super(message);
  }

  public ParameterizedTestMismatchException(final String message, final Throwable cause) {
    super(message, cause);
  }
}

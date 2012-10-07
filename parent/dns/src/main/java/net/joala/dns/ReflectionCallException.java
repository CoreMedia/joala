package net.joala.dns;

/**
 * @since 10/7/12
 */
final class ReflectionCallException extends Exception {
  ReflectionCallException() {
  }

  ReflectionCallException(final Throwable cause) {
    super(cause);
  }

  ReflectionCallException(final String message) {
    super(message);
  }

  ReflectionCallException(final String message, final Throwable cause) {
    super(message, cause);
  }

}

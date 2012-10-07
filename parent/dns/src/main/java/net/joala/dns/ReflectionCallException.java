package net.joala.dns;

/**
 * <p>
 * Signals a call triggered with reflections failed.
 * </p>
 *
 * @since 10/7/12
 */
final class ReflectionCallException extends Exception {
  /**
   * Constructor.
   *
   * @param cause cause for the failure
   */
  ReflectionCallException(final Throwable cause) {
    super(cause);
  }
}

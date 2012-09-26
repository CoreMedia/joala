package net.joala.bdd.reference;

/**
 * <p>
 * Denotes that a property of a reference you try to read is not set.
 * </p>
 *
 * @since 6/5/12
 */
public class PropertyNotSetException extends RuntimeException {
  public PropertyNotSetException() {
  }

  public PropertyNotSetException(final Throwable cause) {
    super(cause);
  }

  public PropertyNotSetException(final String message) {
    super(message);
  }

  public PropertyNotSetException(final String message, final Throwable cause) {
    super(message, cause);
  }
}

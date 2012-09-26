package net.joala.bdd.reference;

/**
 * <p>
 * Denotes that you try to reset a property of a reference.
 * </p>
 * @since 6/5/12
 */
public class PropertyAlreadySetException extends RuntimeException {
  public PropertyAlreadySetException() {
  }

  public PropertyAlreadySetException(final Throwable cause) {
    super(cause);
  }

  public PropertyAlreadySetException(final String message) {
    super(message);
  }

  public PropertyAlreadySetException(final String message, final Throwable cause) {
    super(message, cause);
  }
}

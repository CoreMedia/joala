package net.joala.bdd.reference;

/**
 * <p>
 * Denotes that a {@link Reference} already has a value. You must not set a reference value twice.
 * </p>
 *
 * @since 6/5/12
 */
public class ReferenceAlreadyBoundException extends RuntimeException {
  public ReferenceAlreadyBoundException() {
  }

  public ReferenceAlreadyBoundException(final Throwable cause) {
    super(cause);
  }

  public ReferenceAlreadyBoundException(final String message) {
    super(message);
  }

  public ReferenceAlreadyBoundException(final String message, final Throwable cause) {
    super(message, cause);
  }
}

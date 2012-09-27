package net.joala.bdd.reference;

import javax.annotation.Nullable;

/**
 * <p>
 * Denotes that a {@link Reference} already has a value. You must not set a reference value twice.
 * </p>
 *
 * @since 6/5/12
 */
public class ReferenceAlreadyBoundException extends RuntimeException {

  /**
   * <p>
   * Constructor setting a message.
   * </p>
   *
   * @param message a failure message
   */
  public ReferenceAlreadyBoundException(@Nullable final String message) {
    super(message);
  }

}

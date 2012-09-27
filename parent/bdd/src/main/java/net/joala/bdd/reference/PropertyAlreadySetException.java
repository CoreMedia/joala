package net.joala.bdd.reference;

import javax.annotation.Nullable;

/**
 * <p>
 * Denotes that you try to reset a property of a reference.
 * </p>
 *
 * @since 6/5/12
 */
public class PropertyAlreadySetException extends RuntimeException {

  /**
   * <p>
   * Constructor setting a message.
   * </p>
   *
   * @param message a failure message
   */
  public PropertyAlreadySetException(@Nullable final String message) {
    super(message);
  }

}

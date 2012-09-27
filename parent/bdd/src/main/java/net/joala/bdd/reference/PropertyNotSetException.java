package net.joala.bdd.reference;

import javax.annotation.Nullable;

/**
 * <p>
 * Denotes that a property of a reference you try to read is not set.
 * </p>
 *
 * @since 6/5/12
 */
public class PropertyNotSetException extends RuntimeException {

  /**
   * <p>
   * Constructor setting a message.
   * </p>
   *
   * @param message a failure message
   */
  public PropertyNotSetException(@Nullable final String message) {
    super(message);
  }

}

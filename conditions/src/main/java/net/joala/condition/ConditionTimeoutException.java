package net.joala.condition;

import javax.annotation.Nullable;

/**
 * <p>
 * Exception which signals that a condition is not fulfilled within time.
 * </p>
 *
 * @since 8/23/12
 */
public final class ConditionTimeoutException extends RuntimeException {
  public ConditionTimeoutException(@Nullable final String message, @Nullable final Throwable cause) {
    super(message, cause);
  }
}

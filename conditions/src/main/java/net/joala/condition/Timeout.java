package net.joala.condition;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * A timeout which can be converted to different time-units.
 * </p>
 *
 * @since 8/22/12
 */
public interface Timeout {
  /**
   * Get the timeout in the given unit.
   *
   * @param targetUnit the timeunit to use
   * @return timeout in the given unit
   */
  @Nonnegative
  long in(@Nonnull TimeUnit targetUnit);

  /**
   * <p>
   * Get the timeout in the given unit adjusted by the given factor.
   * </p>
   *
   * @param targetUnit the timeunit to use
   * @param factor     factor to adjust the timeout
   * @return timeout adjust by factor
   */
  @Nonnegative
  long in(@Nonnull TimeUnit targetUnit, @Nonnegative double factor);
}

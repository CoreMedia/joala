package net.joala.condition;

/**
 * <p>
 * Wait for a condition to become true - or fail if timeout is exceeded.
 * </p>
 *
 * @since 8/23/12
 */
interface ConditionWait {
  /**
   * <p>
   * Wait until condition becomes true. Or fail on timeout.
   * </p>
   */
  void until();
}

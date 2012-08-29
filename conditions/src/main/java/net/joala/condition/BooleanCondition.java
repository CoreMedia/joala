package net.joala.condition;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Specialized condition for boolean states (true/false). Offers convenience
 * methods like {@link #assumeTrue()}.
 *
 * @since 2/27/12
 */
public interface BooleanCondition extends Condition<Boolean> {
  /**
   * Convenience method to assume the condition to become true.
   */
  void assumeTrue();

  /**
   * Convenience method to assume the condition to become false.
   */
  void assumeFalse();

  /**
   * Convenience method to assert the condition to become true.
   */
  void assertTrue();

  /**
   * Convenience method to assert the condition to become false.
   */
  void assertFalse();

  @Override
  @Nonnull
  BooleanCondition withMessage(@Nullable String message);

  @Override
  @Nonnull
  BooleanCondition withTimeoutFactor(@Nonnegative double factor);
}

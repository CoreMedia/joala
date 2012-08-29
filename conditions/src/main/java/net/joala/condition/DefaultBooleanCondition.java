package net.joala.condition;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @since 2/26/12
 */
public class DefaultBooleanCondition extends DefaultCondition<Boolean> implements BooleanCondition {
  public DefaultBooleanCondition(@Nonnull final Expression<Boolean> expression, @Nonnull final Timeout timeout) {
    super(expression, timeout);
  }

  @Override
  public final void assumeTrue() {
    assumeEquals(true);
  }

  @Override
  public final void assumeFalse() {
    assumeEquals(false);
  }

  @Override
  public final void assertTrue() {
    assertEquals(true);
  }

  @Override
  public final void assertFalse() {
    assertEquals(false);
  }

  @Override
  @Nonnull
  public DefaultBooleanCondition runFinally(@Nullable final Runnable runnable) {
    super.runFinally(runnable);
    return this;
  }

  @Override
  @Nonnull
  public DefaultBooleanCondition runBefore(@Nullable final Runnable runnable) {
    super.runBefore(runnable);
    return this;
  }

  @Override
  @Nonnull
  public DefaultBooleanCondition withMessage(@Nullable final String message) {
    super.withMessage(message);
    return this;
  }

  @Override
  @Nonnull
  public DefaultBooleanCondition withTimeoutFactor(@Nonnegative final double factor) {
    super.withTimeoutFactor(factor);
    return this;
  }
}

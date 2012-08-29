package net.joala.condition;

import org.hamcrest.Matcher;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeThat;

/**
 * <p>
 * Fail-strategy to fail with timeout exception.
 * </p>
 *
 * @since 8/23/12
 */
class ConditionWaitTimeoutFailStrategy extends AbstractConditionWaitFailStrategy {
  @Override
  public void fail(@Nonnull final String reason, @Nonnull final ConditionFunction<?> function, @Nonnull final ExpressionEvaluationException exception, @Nonnegative final long consumedMillis) {
    throw new ConditionTimeoutException(addTimeoutDescription(reason, function, consumedMillis), exception);
  }

  @Override
  public <T> void fail(@Nonnull final String reason, @Nonnull final ConditionFunction<T> function, @Nonnull final Matcher<? super T> matcher, @Nonnegative final long consumedMillis) {
    try {
      assumeThat(function.getCached(), matcher);
    } catch (Exception e) {
      throw new ConditionTimeoutException(addTimeoutDescription(reason, function, consumedMillis), e);
    }
  }

}

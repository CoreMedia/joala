package net.joala.condition;

import org.hamcrest.Matcher;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * <p>
 * Strategy to fail with an assertion error if the condition is not fulfilled.
 * </p>
 *
 * @since 8/23/12
 */
class ConditionWaitAssertionFailStrategy extends AbstractConditionWaitFailStrategy {
  @Override
  public void fail(@Nonnull final String reason, @Nonnull final ConditionFunction<?> function, @Nonnull final ExpressionEvaluationException exception, @Nonnegative final long consumedMillis) {
    assertThat(addTimeoutDescription(reason, function, consumedMillis), exception, new ConditionWaitFailNoExceptionMatcher(function));
  }

  @Override
  public <T> void fail(@Nonnull final String reason, @Nonnull final ConditionFunction<T> function, @Nonnull final Matcher<? super T> matcher, @Nonnegative final long consumedMillis) {
    assertThat(addTimeoutDescription(reason, function, consumedMillis), function.getCached(), matcher);
  }
}

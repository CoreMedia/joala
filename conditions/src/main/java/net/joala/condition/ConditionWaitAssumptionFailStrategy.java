package net.joala.condition;

import org.hamcrest.Matcher;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static net.joala.matcher.decorator.EnhanceDescriptionBy.enhanceDescriptionBy;
import static org.junit.Assume.assumeThat;

/**
 * <p>
 * Strategy to fail with an assumption violation if the condition is not fulfilled.
 * </p>
 *
 * @since 8/23/12
 */
class ConditionWaitAssumptionFailStrategy extends AbstractConditionWaitFailStrategy {
  @Override
  public void fail(@Nonnull final String reason, @Nonnull final ConditionFunction<?> function, @Nonnull final ExpressionEvaluationException exception, @Nonnegative final long consumedMillis) {
    // enhanceDescriptionBy: Workaround, see https://github.com/KentBeck/junit/pull/489
    assumeThat(exception, enhanceDescriptionBy(addTimeoutDescription(reason, function, consumedMillis), new ConditionWaitFailNoExceptionMatcher(function)));
  }

  @Override
  public <T> void fail(@Nonnull final String reason, @Nonnull final ConditionFunction<T> function, @Nonnull final Matcher<? super T> matcher, @Nonnegative final long consumedMillis) {
    // enhanceDescriptionBy: Workaround, see https://github.com/KentBeck/junit/pull/489
    assumeThat(function.getCached(), enhanceDescriptionBy(addTimeoutDescription(reason, function, consumedMillis), matcher));
  }

}

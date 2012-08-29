package net.joala.condition;

import org.hamcrest.Matcher;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * <p>
 * Strategy what to do if a condition does not get fulfilled within time. Possible options
 * are to signal just the timeout or that an assertion/assumption failed.
 * </p>
 *
 * @since 8/23/12
 */
interface ConditionWaitFailStrategy {
  /**
   * Makes a condition fail because the expected value did not get returned in time.
   *
   * @param reason         reason of the failure
   * @param function       function evaluated
   * @param matcher        the matcher which did not match
   * @param consumedMillis consumed milliseconds
   */
  <T> void fail(@Nonnull String reason, @Nonnull ConditionFunction<T> function, @Nonnull Matcher<? super T> matcher, @Nonnegative long consumedMillis);

  /**
   * Makes a condition fail because the expected value could not be retrieved because of repeating
   * evaluation exceptions.
   *
   * @param reason    reason of the failure
   * @param function  function evaluated
   * @param exception last exception which got caught
   * @param consumedMillis consumed milliseconds
   */
  void fail(@Nonnull String reason, @Nonnull ConditionFunction<?> function, @Nonnull ExpressionEvaluationException exception, @Nonnegative long consumedMillis);
}

package net.joala.condition;

import java.util.concurrent.TimeoutException;

/**
 * @since 8/27/12
 */
public class ConditionWaitTimeoutFailStrategyTest extends ConditionWaitFailStrategyTest<ConditionWaitTimeoutFailStrategy, ConditionTimeoutException> {
  @Override
  protected Class<ConditionTimeoutException> getRaisedExceptionType() {
    return ConditionTimeoutException.class;
  }

  @Override
  protected ConditionWaitTimeoutFailStrategy getFailStrategy() {
    return new ConditionWaitTimeoutFailStrategy();
  }
}

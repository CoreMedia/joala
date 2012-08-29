package net.joala.condition;

/**
 * @since 8/26/12
 */
public class ConditionWaitAssertionFailStrategyTest extends ConditionWaitFailStrategyTest<ConditionWaitAssertionFailStrategy, AssertionError> {
  @Override
  protected Class<AssertionError> getRaisedExceptionType() {
    return AssertionError.class;
  }

  @Override
  protected ConditionWaitAssertionFailStrategy getFailStrategy() {
    return new ConditionWaitAssertionFailStrategy();
  }
}

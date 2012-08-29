package net.joala.condition;

import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;

/**
 * @since 8/26/12
 */
public class ConditionWaitAssumptionFailStrategyTest extends ConditionWaitFailStrategyTest<ConditionWaitAssumptionFailStrategy, AssumptionViolatedException> {
  @Override
  protected Class<AssumptionViolatedException> getRaisedExceptionType() {
    return AssumptionViolatedException.class;
  }

  @Override
  protected ConditionWaitAssumptionFailStrategy getFailStrategy() {
    return new ConditionWaitAssumptionFailStrategy();
  }
}

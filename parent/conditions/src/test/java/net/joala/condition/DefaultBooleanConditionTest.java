/*
 * Copyright 2012 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.joala.condition;

import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Tests {@link DefaultBooleanCondition}.
 * </p>
 *
 * @since 9/4/12
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultBooleanConditionTest {
  @Mock
  private Expression<Boolean> expression;
  @Mock
  private Timeout timeout;
  @Mock
  private Runnable runnable;

  @Test
  public void assumeTrue_should_pass_if_expression_is_true() throws Exception {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(true);
    try {
      condition.assumeTrue();
    } catch (AssumptionViolatedException ignored) {
      fail("Should have passed.");
    }
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void assumeTrue_should_fail_if_expression_is_false() throws Exception {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(false);
    when(timeout.in(any(TimeUnit.class), anyDouble())).thenReturn(1l);
    try {
      condition.assumeTrue();
      fail("Should have failed.");
    } catch (AssumptionViolatedException ignored) {
    }
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void assumeFalse_should_pass_if_expression_is_false() throws Exception {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(false);
    try {
      condition.assumeFalse();
    } catch (AssumptionViolatedException ignored) {
      fail("Should have passed.");
    }
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void assumeFalse_should_fail_if_expression_is_true() throws Exception {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(true);
    when(timeout.in(any(TimeUnit.class), anyDouble())).thenReturn(1l);
    try {
      condition.assumeFalse();
      fail("Should have failed.");
    } catch (AssumptionViolatedException ignored) {
    }
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void assertTrue_should_pass_if_expression_is_true() throws Exception {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(true);
    try {
      condition.assertTrue();
    } catch (AssertionError ignored) {
      fail("Should have passed.");
    }
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void assertTrue_should_fail_if_expression_is_false() throws Exception {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(false);
    when(timeout.in(any(TimeUnit.class), anyDouble())).thenReturn(1l);
    boolean assertionPassed = false;
    try {
      condition.assertTrue();
      assertionPassed = true;
    } catch (AssertionError ignored) {
    }
    assertFalse("Should have failed.", assertionPassed);
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void assertFalse_should_pass_if_expression_is_false() throws Exception {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(false);
    try {
      condition.assertFalse();
    } catch (AssertionError ignored) {
      fail("Should have passed.");
    }
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void assertFalse_should_fail_if_expression_is_true() throws Exception {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(true);
    when(timeout.in(any(TimeUnit.class), anyDouble())).thenReturn(1l);
    boolean assertionPassed = false;
    try {
      condition.assertFalse();
      assertionPassed = true;
    } catch (AssertionError ignored) {
    }
    assertFalse("Should have failed.", assertionPassed);
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void runFinally_should_run_after_success() throws Exception {
    final DefaultBooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    condition.runFinally(runnable);
    when(expression.get()).thenReturn(true);
    try {
      condition.assertTrue();
    } catch (AssertionError ignored) {
    }
    final InOrder inOrder = inOrder(expression, runnable);
    inOrder.verify(expression).get();
    inOrder.verify(runnable).run();
  }

  @Test
  public void runFinally_should_run_after_failure() throws Exception {
    final DefaultBooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    condition.runFinally(runnable);
    when(expression.get()).thenReturn(true);
    try {
      condition.assertFalse();
    } catch (AssertionError ignored) {
    }
    final InOrder inOrder = inOrder(expression, runnable);
    inOrder.verify(expression, atLeastOnce()).get();
    inOrder.verify(runnable).run();
  }

  @Test
  public void runBefore_should_run() throws Exception {
    final DefaultBooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    condition.runBefore(runnable);
    when(expression.get()).thenReturn(true);
    try {
      condition.assertTrue();
    } catch (AssertionError ignored) {
    }
    final InOrder inOrder = inOrder(runnable, expression);
    inOrder.verify(runnable).run();
    inOrder.verify(expression).get();
  }
}

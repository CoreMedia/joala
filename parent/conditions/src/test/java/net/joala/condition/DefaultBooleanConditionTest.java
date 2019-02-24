/*
 * Copyright 2012 CoreMedia AG
 *
 * This file is part of Joala.
 *
 * Joala is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Joala is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Joala.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.condition;

import net.joala.condition.timing.WaitTimeoutException;
import net.joala.expression.Expression;
import net.joala.time.Timeout;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
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
  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private Expression<Boolean> expression;
  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private Timeout timeout;
  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private Runnable runnable;

  @Test
  public void assumeTrue_should_pass_if_expression_is_true() {
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
  public void assumeTrue_should_fail_if_expression_is_false() {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(false);
    when(timeout.in(any(TimeUnit.class), anyDouble())).thenReturn(1L);
    try {
      condition.assumeTrue();
      fail("Should have failed.");
    } catch (AssumptionViolatedException ignored) {
    }
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void assumeFalse_should_pass_if_expression_is_false() {
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
  public void assumeFalse_should_fail_if_expression_is_true() {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(true);
    when(timeout.in(any(TimeUnit.class), anyDouble())).thenReturn(1L);
    try {
      condition.assumeFalse();
      fail("Should have failed.");
    } catch (AssumptionViolatedException ignored) {
    }
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void assertTrue_should_pass_if_expression_is_true() {
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
  public void waitUntilTrue_should_pass_if_expression_is_true() {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(true);
    try {
      condition.waitUntilTrue();
    } catch (WaitTimeoutException ignored) {
      fail("Should have passed.");
    }
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void assertTrue_should_fail_if_expression_is_false() {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(false);
    when(timeout.in(any(TimeUnit.class), anyDouble())).thenReturn(1L);
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
  public void waitUntilTrue_should_fail_if_expression_is_false() {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(false);
    when(timeout.in(any(TimeUnit.class), anyDouble())).thenReturn(1L);
    boolean passed = false;
    try {
      condition.waitUntilTrue();
      passed = true;
    } catch (WaitTimeoutException ignored) {
    }
    assertFalse("Should have failed.", passed);
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void assertFalse_should_pass_if_expression_is_false() {
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
  public void waitUntilFalse_should_pass_if_expression_is_false() {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(false);
    try {
      condition.waitUntilFalse();
    } catch (WaitTimeoutException ignored) {
      fail("Should have passed.");
    }
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void assertFalse_should_fail_if_expression_is_true() {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(true);
    when(timeout.in(any(TimeUnit.class), anyDouble())).thenReturn(1L);
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
  public void waitUntilFalse_should_fail_if_expression_is_true() {
    final BooleanCondition condition = new DefaultBooleanCondition(expression, timeout);
    when(expression.get()).thenReturn(true);
    when(timeout.in(any(TimeUnit.class), anyDouble())).thenReturn(1L);
    boolean passed = false;
    try {
      condition.waitUntilFalse();
      passed = true;
    } catch (WaitTimeoutException ignored) {
    }
    assertFalse("Should have failed.", passed);
    verify(expression, atLeastOnce()).get();
  }

  @Test
  public void runFinally_should_run_after_success() {
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
  public void runFinally_should_run_after_failure() {
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
  public void runBefore_should_run() {
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

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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.condition;

import net.joala.condition.timing.WaitFactory;
import net.joala.condition.timing.WaitTimeoutException;
import net.joala.data.DataProvider;
import net.joala.data.random.DefaultRandomStringProvider;
import net.joala.expression.Expression;
import net.joala.expression.ExpressionEvaluationException;
import net.joala.time.Timeout;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static net.joala.matcher.exception.MessageContains.messageContains;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@link DefaultCondition}.
 *
 * @since 8/29/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
@RunWith(MockitoJUnitRunner.class)
public class DefaultConditionTest {
  @Mock
  private Expression<String> stringExpression;
  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private Timeout anyTimeout;
  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private WaitFactory anyWaitFactory;

  @Before
  public void setUp() throws Exception {
    condition = new DefaultCondition<String>(expression, timeout);
    expressionValue = EXPRESSION_VALUE_PROVIDER.get();
    when(expression.get()).thenReturn(expressionValue);
    when(timeout.in(any(TimeUnit.class), any(Double.class))).thenReturn(0L);
  }

  @Test
  public void get_should_return_expression_value() throws Exception {
    final String conditionValue = condition.get();
    verify(expression).get();
    assertEquals("Expression's value should have been returned.", expressionValue, conditionValue);
  }

  @Test
  public void await_noarg_should_return_expression_value() throws Exception {
    final String conditionValue = condition.await();
    assertEquals("Expression's value should have been returned.", expressionValue, conditionValue);
  }

  @Test
  public void await_with_matcher_should_return_expression_value() throws Exception {
    final String conditionValue = condition.await(anything());
    assertEquals("Expression's value should have been returned.", expressionValue, conditionValue);
  }

  @Test
  public void await_should_evaluate_expression_only_once_on_immediate_success() throws Exception {
    condition.await();
    verify(expression).get();
  }

  @Test(expected = WaitTimeoutException.class)
  public void await_should_timeout_if_expression_does_not_match() throws Exception {
    condition.await(not(anything()));
  }

  @Test
  public void assume_should_timeout_if_expression_does_not_match() throws Exception {
    try {
      condition.assumeThat(not(anything()));
      fail("AssumptionViolatedException should have been thrown.");
    } catch (AssumptionViolatedException ignored) {
      // fine
    }
  }

  @Test
  public void assert_should_timeout_if_expression_does_not_match() throws Exception {
    try {
      condition.assertThat(not(anything()));
      fail("AssertionError should have been thrown.");
    } catch (AssertionError ignored) {
      // fine
    }
  }

  @Test
  public void run_before_should_be_executed_on_await() throws Exception {
    ((FailSafeCondition<String>) condition).runBefore(runnable);
    verify(runnable, times(0)).run();
    condition.await(anything());
    verify(runnable, times(1)).run();
  }

  @Test
  public void run_before_should_be_executed_on_assume() throws Exception {
    ((FailSafeCondition<String>) condition).runBefore(runnable);
    verify(runnable, times(0)).run();
    condition.assumeThat(anything());
    verify(runnable, times(1)).run();
  }

  @Test
  public void run_before_should_be_executed_on_assertion() throws Exception {
    ((FailSafeCondition<String>) condition).runBefore(runnable);
    verify(runnable, times(0)).run();
    condition.assumeThat(anything());
    verify(runnable, times(1)).run();
  }

  @Test
  public void run_finally_should_be_executed_on_await_success() throws Exception {
    ((FailSafeCondition<String>) condition).runFinally(runnable);
    verify(runnable, times(0)).run();
    condition.await(anything());
    verify(runnable, times(1)).run();
  }

  @Test
  public void run_finally_should_be_executed_on_assume_success() throws Exception {
    ((FailSafeCondition<String>) condition).runFinally(runnable);
    verify(runnable, times(0)).run();
    condition.assumeThat(anything());
    verify(runnable, times(1)).run();
  }

  @Test
  public void run_finally_should_be_executed_on_assertion_success() throws Exception {
    ((FailSafeCondition<String>) condition).runFinally(runnable);
    verify(runnable, times(0)).run();
    condition.assumeThat(anything());
    verify(runnable, times(1)).run();
  }

  @Test
  public void run_finally_should_be_executed_on_await_failure() throws Exception {
    ((FailSafeCondition<String>) condition).runFinally(runnable);
    verify(runnable, times(0)).run();
    try {
      condition.await(not(anything()));
    } catch (Exception ignored) {
    }
    verify(runnable, times(1)).run();
  }

  @Test
  public void run_finally_should_be_executed_on_assume_failure() throws Exception {
    ((FailSafeCondition<String>) condition).runFinally(runnable);
    verify(runnable, times(0)).run();
    try {
      condition.assumeThat(not(anything()));
    } catch (Exception ignored) {
    }
    verify(runnable, times(1)).run();
  }

  @Test
  public void run_finally_should_be_executed_on_assertion_failure() throws Exception {
    ((FailSafeCondition<String>) condition).runFinally(runnable);
    verify(runnable, times(0)).run();
    try {
      condition.assumeThat(not(anything()));
    } catch (Exception ignored) {
    }
    verify(runnable, times(1)).run();
  }

  @SuppressWarnings("NewExceptionWithoutArguments")
  @Test
  public void run_finally_should_be_executed_on_await_exception() throws Exception {
    ((FailSafeCondition<String>) condition).runFinally(runnable);
    when(expression.get()).thenThrow(new ExpressionEvaluationException());
    verify(runnable, times(0)).run();
    try {
      condition.await(not(anything()));
    } catch (Exception ignored) {
    }
    verify(runnable, times(1)).run();
  }

  @SuppressWarnings("NewExceptionWithoutArguments")
  @Test
  public void run_finally_should_be_executed_on_assume_exception() throws Exception {
    ((FailSafeCondition<String>) condition).runFinally(runnable);
    when(expression.get()).thenThrow(new ExpressionEvaluationException());
    verify(runnable, times(0)).run();
    try {
      condition.assumeThat(not(anything()));
    } catch (Exception ignored) {
    }
    verify(runnable, times(1)).run();
  }

  @SuppressWarnings("NewExceptionWithoutArguments")
  @Test
  public void run_finally_should_be_executed_on_assertion_exception() throws Exception {
    ((FailSafeCondition<String>) condition).runFinally(runnable);
    when(expression.get()).thenThrow(new ExpressionEvaluationException());
    verify(runnable, times(0)).run();
    try {
      condition.assumeThat(not(anything()));
    } catch (Exception ignored) {
    }
    verify(runnable, times(1)).run();
  }

  @Test
  public void await_should_apply_timeout_factor_to_timeout() throws Exception {
    final double factor = Math.random();
    condition.withTimeoutFactor(factor);
    condition.await();
    verify(timeout).in(any(TimeUnit.class), eq(factor));
  }

  @Test
  public void assume_should_apply_timeout_factor_to_timeout() throws Exception {
    final double factor = Math.random();
    condition.withTimeoutFactor(factor);
    condition.assumeThat(anything());
    verify(timeout).in(any(TimeUnit.class), eq(factor));
  }

  @Test
  public void assert_should_apply_timeout_factor_to_timeout() throws Exception {
    final double factor = Math.random();
    condition.withTimeoutFactor(factor);
    condition.assertThat(anything());
    verify(timeout).in(any(TimeUnit.class), eq(factor));
  }

  @SuppressWarnings("NewExceptionWithoutArguments")
  @Test
  public void message_should_be_contained_in_exception_on_await_exception() throws Exception {
    ((FailSafeCondition<String>) condition).runFinally(runnable);
    when(expression.get()).thenThrow(new ExpressionEvaluationException());
    final String message = CONDITION_MESSAGE_PROVIDER.get();
    condition.withMessage(message);
    try {
      condition.await(not(anything()));
    } catch (Exception e) {
      assertThat(e, messageContains(message, true));
    }
  }

  @SuppressWarnings("NewExceptionWithoutArguments")
  @Test
  public void message_should_be_contained_in_exception_on_assume_exception() throws Exception {
    ((FailSafeCondition<String>) condition).runFinally(runnable);
    when(expression.get()).thenThrow(new ExpressionEvaluationException());
    final String message = CONDITION_MESSAGE_PROVIDER.get();
    condition.withMessage(message);
    try {
      condition.assumeThat(not(anything()));
    } catch (Exception e) {
      assertThat(e, messageContains(message, true));
    }
  }

  @SuppressWarnings("NewExceptionWithoutArguments")
  @Test
  public void message_should_be_contained_in_exception_on_assertion_exception() throws Exception {
    ((FailSafeCondition<String>) condition).runFinally(runnable);
    when(expression.get()).thenThrow(new ExpressionEvaluationException());
    final String message = CONDITION_MESSAGE_PROVIDER.get();
    condition.withMessage(message);
    try {
      condition.assumeThat(not(anything()));
    } catch (Exception e) {
      assertThat(e, messageContains(message, true));
    }
  }

  private static final DataProvider<String> EXPRESSION_VALUE_PROVIDER = new DefaultRandomStringProvider().prefix("expressionValue").fixate();
  private static final DataProvider<String> CONDITION_MESSAGE_PROVIDER = new DefaultRandomStringProvider().prefix("conditionMessage").fixate();

  private Condition<String> condition;
  private String expressionValue;
  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private Timeout timeout;
  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private Expression<String> expression;
  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private Runnable runnable;

}

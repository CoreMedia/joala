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

import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;

import java.util.concurrent.TimeUnit;

import static net.joala.condition.RandomData.randomString;
import static net.joala.matcher.exception.MessageContains.messageContains;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @since 8/29/12
 */
public class DefaultConditionTest {

  private Runnable runnable;

  @Before
  public void setUp() throws Exception {
    expression = mock(StringExpression.class);
    timeout = mock(Timeout.class);
    condition = new DefaultCondition<String>(expression, timeout);
    expressionValue = randomString("expressionValue");
    when(expression.get()).thenReturn(expressionValue);
    when(timeout.in(any(TimeUnit.class), any(Double.class))).thenReturn(0l);
    runnable = mock(Runnable.class);
  }

  @Test
  public void constructor_should_accept_nonnull_expression() throws Exception {
    condition.get();
    verify(expression).get();
  }

  @SuppressWarnings("ConstantConditions")
  @Test(expected = NullPointerException.class)
  public void constructor_should_throw_nullpointer_exception_if_expression_is_null() throws Exception {
    new DefaultCondition<String>(null, timeout);
  }

  @Test
  public void constructor_should_accept_nonnull_timeout() throws Exception {
    condition.await();
    verify(timeout).in(any(TimeUnit.class), any(Double.class));
  }

  @SuppressWarnings("ConstantConditions")
  @Test(expected = NullPointerException.class)
  public void constructor_should_throw_nullpointer_exception_if_timeout_is_null() throws Exception {
    new DefaultCondition<String>(expression, null);
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

  @Test(expected = ConditionTimeoutException.class)
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
    final String message = randomString("conditionMessage");
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
    final String message = randomString("conditionMessage");
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
    final String message = randomString("conditionMessage");
    condition.withMessage(message);
    try {
      condition.assumeThat(not(anything()));
    } catch (Exception e) {
      assertThat(e, messageContains(message, true));
    }
  }

  private StringExpression expression;
  private Timeout timeout;
  private Condition<String> condition;
  private String expressionValue;

  private static class StringExpression implements Expression<String> {
    @Override
    public void describeTo(final Description description) {
    }

    @Override
    public String get() {
      return null;
    }
  }

}

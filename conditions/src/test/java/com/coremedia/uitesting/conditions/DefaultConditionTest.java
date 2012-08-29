package com.coremedia.uitesting.conditions;

import net.joala.condition.AbstractExpression;
import net.joala.condition.Condition;
import net.joala.condition.DefaultCondition;
import net.joala.condition.DefaultTimeoutProvider;
import net.joala.condition.ExpressionEvaluationException;
import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;
import static org.junit.internal.matchers.StringContains.containsString;

/**
 * Tests {@link net.joala.condition.DefaultCondition}.
 *
 * @since 2/29/12
 */
public class DefaultConditionTest {
  private static final Random RANDOM = new Random(System.currentTimeMillis());
  private static final int MESSAGE_LENGTH = 20;
  private DefaultTimeoutProvider timeoutProvider;
  private Integer expressionValue;
  private final String exceptionMessage = random(MESSAGE_LENGTH);
  private final String conditionMessage = random(MESSAGE_LENGTH);
  private long getCount;
  private AbstractExpression<Integer> successfulExpression;
  private AbstractExpression<Integer> failingExpression;

  @Before
  public void setUp() throws Exception {
    timeoutProvider = new DefaultTimeoutProvider();
    timeoutProvider.setMilliseconds(2l);
    expressionValue = RANDOM.nextInt();
    getCount = 0;
    successfulExpression = new AbstractExpression<Integer>() {
      @Override
      public Integer get() {
        getCount++;
        return expressionValue;
      }
    };
    failingExpression = new AbstractExpression<Integer>() {
      @Override
      public Integer get() {
        throw new ExpressionEvaluationException(exceptionMessage);
      }
    };
  }

  @Test
  public void shouldDirectlyRetrieveExpressionValue() {
    final DefaultCondition<Integer> condition = new DefaultCondition<Integer>(timeoutProvider, successfulExpression);
    final Integer conditionResult = condition.get();
    assertEquals("Condition result should match expression result.", expressionValue, conditionResult);
  }

  @Test
  public void shouldSuccessfullyAwaitExpressionResult() throws Exception {
    final DefaultCondition<Integer> condition = new DefaultCondition<Integer>(timeoutProvider, successfulExpression);
    final Integer conditionResult = condition.await();
    assertEquals("Condition result should match expression result.", expressionValue, conditionResult);
    assertEquals("Expression should have been evaluated only once.", 1, getCount);
  }

  @Test
  public void shouldSuccessfullyAwaitMatchingExpressionResult() throws Exception {
    final DefaultCondition<Integer> condition = new DefaultCondition<Integer>(timeoutProvider, successfulExpression);
    final Integer conditionResult = condition.await(equalTo(expressionValue));
    assertEquals("Condition result should match expression result.", expressionValue, conditionResult);
    assertEquals("Expression should have been evaluated only once.", 1, getCount);
  }

  @Test
  public void shouldSuccessfullyAssumeEqualsMatchingExpressionResult() throws Exception {
    final Condition<Integer> condition = new DefaultCondition<Integer>(timeoutProvider, successfulExpression);
    try {
      condition.assumeEquals(expressionValue);
    } catch (AssumptionViolatedException ignored) {
      fail("Should have thrown no assumption violated exception.");
    }
  }

  @Test
  public void shouldSuccessfullyAssumeThatMatchingExpressionResult() throws Exception {
    final Condition<Integer> condition = new DefaultCondition<Integer>(timeoutProvider, successfulExpression);
    try {
      condition.assumeThat(equalTo(expressionValue));
    } catch (AssumptionViolatedException ignored) {
      fail("Should have thrown no assumption violated exception.");
    }
  }

  @Test
  public void shouldSuccessfullyAssertEqualsMatchingExpressionResult() throws Exception {
    final Condition<Integer> condition = new DefaultCondition<Integer>(timeoutProvider, successfulExpression);
    try {
      condition.assertEquals(expressionValue);
    } catch (AssertionError ignored) {
      fail("Should have thrown no assertion error.");
    }
  }

  @Test
  public void shouldSuccessfullyAssertThatMatchingExpressionResult() throws Exception {
    final Condition<Integer> condition = new DefaultCondition<Integer>(timeoutProvider, successfulExpression);
    try {
      condition.assertThat(equalTo(expressionValue));
    } catch (AssertionError ignored) {
      fail("Should have thrown no assertion error.");
    }
  }

  @Test
  public void shouldFailAssertionOnContinuousExceptions() throws Exception {
    final Condition<Integer> condition = new DefaultCondition<Integer>(timeoutProvider, failingExpression);
    boolean exceptionRaised = false;
    try {
      condition.assertEquals(expressionValue);
    } catch (AssertionError e) {
      exceptionRaised = true;
      final StringWriter stringWriter = new StringWriter();
      e.printStackTrace(new PrintWriter(stringWriter));
      assertThat("Assertion error should contain hint for failure.", stringWriter.toString(), containsString(exceptionMessage));
      assertThat("Assertion error should contain expected value.", stringWriter.toString(), containsString(String.valueOf(expressionValue)));
    }
    assertTrue("Exception should have been raised.", exceptionRaised);
  }

  @Test
  public void shouldFailAssertionOnContinuousExceptionsWithCustomMessage() throws Exception {
    final DefaultCondition<Integer> condition = new DefaultCondition<Integer>(timeoutProvider, failingExpression)
            .withMessage(conditionMessage);
    boolean exceptionRaised = false;
    try {
      condition.assertEquals(expressionValue);
      fail("Should have thrown assertionError as all calls failed with evaluation exception.");
    } catch (AssertionError e) {
      exceptionRaised = true;
      final StringWriter stringWriter = new StringWriter();
      e.printStackTrace(new PrintWriter(stringWriter));
      assertThat("Assertion error should contain message configured for condition.", stringWriter.toString(), containsString(conditionMessage));
    }
    assertTrue("Exception should have been raised.", exceptionRaised);
  }

  @Test
  public void shouldRunFinallyOnContinuousExceptions() throws Exception {
    final int[] count = new int[]{0};
    final DefaultCondition<Integer> condition = new DefaultCondition<Integer>(timeoutProvider, failingExpression)
            .runFinally(new Runnable() {
              @Override
              public void run() {
                count[0]++;
              }
            });
    try {
      condition.assertEquals(expressionValue);
    } catch (AssertionError ignored) {
      // Ignore
    }
    assertEquals("Finally Runnable should have been run exactly once.", 1, count[0]);
  }

  @Test
  public void shouldRunFinallyOnSuccess() throws Exception {
    final int[] count = new int[]{0};
    final DefaultCondition<Integer> condition = new DefaultCondition<Integer>(timeoutProvider, successfulExpression)
            .runFinally(new Runnable() {
              @Override
              public void run() {
                count[0]++;
              }
            });
    try {
      condition.assertEquals(expressionValue);
    } catch (AssertionError ignored) {
      fail("Should have thrown no assertion error.");
    }
    assertEquals("Finally Runnable should have been run exactly once.", 1, count[0]);
  }

  @Test
  public void shouldRespectTimeoutFactorOnFailure() throws Exception {
    final long timeoutMilliseconds = 100l;
    timeoutProvider.setMilliseconds(timeoutMilliseconds);
    long startTimeMillis;
    final long[] endTimeMillis = new long[]{0l};
    final DefaultCondition<Integer> condition = new DefaultCondition<Integer>(timeoutProvider, failingExpression)
            .runFinally(new Runnable() {
              @Override
              public void run() {
                endTimeMillis[0] = System.currentTimeMillis();
              }
            });
    startTimeMillis = System.currentTimeMillis();
    try {
      condition.assertEquals(expressionValue);
    } catch (AssertionError ignored) {
      // Ignore
    }
    long elapsedMillis = endTimeMillis[0] - startTimeMillis;

    assumeThat(elapsedMillis, new CustomTypeSafeMatcher<Long>("timeout with tolerance") {
      @Override
      protected boolean matchesSafely(final Long item) {
        return item >= timeoutMilliseconds && item <= 2l * timeoutMilliseconds;
      }
    });

    final double timeoutFactor = 4.0d;
    condition.withTimeoutFactor(timeoutFactor);

    startTimeMillis = System.currentTimeMillis();
    try {
      condition.assertEquals(expressionValue);
    } catch (AssertionError ignored) {
      // Ignore
    }
    elapsedMillis = endTimeMillis[0] - startTimeMillis;

    assertThat("Timeout Factor should be respected.", elapsedMillis, new CustomTypeSafeMatcher<Long>("timeout with tolerance") {
      @Override
      protected boolean matchesSafely(final Long item) {
        final double weightedTimeout = timeoutMilliseconds * timeoutFactor;
        return item >= weightedTimeout && item <= 2l * weightedTimeout;
      }
    });
  }
}

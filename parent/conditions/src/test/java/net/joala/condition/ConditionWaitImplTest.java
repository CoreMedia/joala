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
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsAnything;
import org.junit.Test;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static java.lang.Math.round;
import static java.lang.String.format;
import static net.joala.condition.ConditionWaitImpl.DECELERATION_FACTOR;
import static net.joala.condition.ConditionWaitImpl.INITIAL_DELAY;
import static net.joala.condition.RandomData.randomPositiveInt;
import static net.joala.condition.RandomData.randomString;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * <p>
 * Tests {@link ConditionWaitImpl}. This test is a complete whitebox test knowing all calls to methods outside.
 * It expects a certain sequence of commands and the test defines all their expected results.
 * </p>
 *
 * @since 8/27/12
 */
public class ConditionWaitImplTest {

  private static final long TIMEOUT_MILLIS = 500l;

  @Test
  public void until_returns_immediately_if_first_evaluation_succeeds() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(ConditionWaitCall.NOW, 0l)
            .add(ConditionWaitCall.NOW, 0l)
            .add(ConditionWaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait();
  }

  @Test
  public void until_retries_once_and_then_succeeds() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(ConditionWaitCall.NOW, 0l)
            .add(ConditionWaitCall.NOW, 0l)
            .add(ConditionWaitCall.FUNCTION_RESULT, false)
            .add(ConditionWaitCall.NOW, 0l)
            .add(ConditionWaitCall.SLEEP, INITIAL_DELAY)
            .add(ConditionWaitCall.NOW, INITIAL_DELAY)
            .add(ConditionWaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait();
  }

  @SuppressWarnings("NewExceptionWithoutArguments")
  @Test
  public void until_retries_once_with_exception_and_then_succeeds() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(ConditionWaitCall.NOW, 0l)
            .add(ConditionWaitCall.NOW, 0l)
            .add(ConditionWaitCall.FUNCTION_RESULT, new ExpressionEvaluationException())
            .add(ConditionWaitCall.NOW, 0l)
            .add(ConditionWaitCall.SLEEP, INITIAL_DELAY)
            .add(ConditionWaitCall.NOW, INITIAL_DELAY)
            .add(ConditionWaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait();
  }

  @Test
  public void until_retries_until_timeout_and_fails() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(ConditionWaitCall.NOW, 0l)
            .add(ConditionWaitCall.NOW, 0l)
            .add(ConditionWaitCall.FUNCTION_RESULT, false)
            .add(ConditionWaitCall.NOW, TIMEOUT_MILLIS * 2l)
            .build();
    assertFailedWait();
  }

  @Test
  public void until_retries_with_exponential_backoff() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(ConditionWaitCall.NOW, 0l)
            .add(ConditionWaitCall.NOW, 0l)
            .add(ConditionWaitCall.FUNCTION_RESULT, false)
            .add(ConditionWaitCall.NOW, INITIAL_DELAY)
            .add(ConditionWaitCall.SLEEP, INITIAL_DELAY)
            .add(ConditionWaitCall.NOW, INITIAL_DELAY)
            .add(ConditionWaitCall.FUNCTION_RESULT, false)
            .add(ConditionWaitCall.NOW, INITIAL_DELAY)
            .add(ConditionWaitCall.SLEEP, round(INITIAL_DELAY * DECELERATION_FACTOR))
            .add(ConditionWaitCall.NOW, round(INITIAL_DELAY * DECELERATION_FACTOR) + INITIAL_DELAY)
            .add(ConditionWaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait();
  }

  @Test
  public void until_polling_should_adopt_to_evaluation_duration() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(ConditionWaitCall.NOW, 0l) // used to calculate deadline
            .add(ConditionWaitCall.NOW, 0l) // used to get timestamp before evaluation
            .add(ConditionWaitCall.FUNCTION_RESULT, false) // call function but fail
            .add(ConditionWaitCall.NOW, INITIAL_DELAY * 2) // get time after evaluation; bigger than last (initial) delay
            .add(ConditionWaitCall.SLEEP, INITIAL_DELAY * 2) // assertion: Now sleep interval should be equal to last duration
            .add(ConditionWaitCall.NOW, INITIAL_DELAY * 2) // play it to the end
            .add(ConditionWaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait();
  }

  @Test
  public void sleep_should_sleep_for_specified_millis() throws Exception {
    final long sleepMillis = 200l;
    final long before = System.currentTimeMillis();
    new ConditionWaitImpl<Object>(randomString(), new TestedConditionFunction(), anything(), 0l, new TestedConditionWaitFailStrategy()).sleep(sleepMillis);
    final long after = System.currentTimeMillis();
    final long delay = after - before;
    assertThat("Wait enough time.", delay, greaterThan(sleepMillis / 2));
  }

  @Test
  public void now_should_return_system_time_millis() throws Exception {
    final long before = System.currentTimeMillis();
    final long now = new ConditionWaitImpl<Object>(randomString(), new TestedConditionFunction(), anything(), 0l, new TestedConditionWaitFailStrategy()).now();
    final long after = System.currentTimeMillis();
    // Check loosely in case the clock drifts.
    assertTrue("now = " + now + " at " + before, before < now + 100);
    assertTrue("now = " + now + " at " + after, after > now - 100);
  }

  @Test
  public void toString_should_contain_parameters_as_hints() throws Exception {
    final int timeoutMillis = randomPositiveInt();
    final String message = randomString();
    final String functionString = randomString();
    final String matcherString = randomString();
    final String failStrategyString = randomString();
    final TestedConditionFunction conditionFunction = new TestedConditionFunction() {
      @Override
      public String toString() {
        return functionString;
      }
    };
    final Matcher<Object> matcher = new IsAnything<Object>() {
      @Override
      public String toString() {
        return matcherString;
      }
    };
    final TestedConditionWaitFailStrategy failStrategy = new TestedConditionWaitFailStrategy() {
      @Override
      public String toString() {
        return failStrategyString;
      }
    };
    final String str = new ConditionWaitImpl<Object>(message, conditionFunction, matcher, timeoutMillis, failStrategy).toString();
    assertThat("All parameters should be contained in toString.",
            str,
            allOf(
                    containsString(message),
                    containsString(functionString),
                    containsString(matcherString),
                    containsString(String.valueOf(timeoutMillis)),
                    containsString(failStrategyString)
            ));
  }

  private LinkedList<ExpectedCall> expectedCalls;

  private void assertSuccessfulWait() {
    final ConditionWait conditionWait = new TestedConditionWaitImpl(randomString());
    conditionWait.until();
    assertThat("All expected commands should have been called. Thus the list of expected commands should be empty.", expectedCalls, Matchers.empty());
  }

  private void assertFailedWait() {
    final ConditionWait conditionWait = new TestedConditionWaitImpl(randomString());
    try {
      conditionWait.until();
      fail("Should have failed with timeout failure.");
    } catch (RuntimeException ignored) {
      // fine
    }
    assertThat("All expected commands should have been called. Thus the list of expected commands should be empty.", expectedCalls, Matchers.empty());
  }

  private static class TestedConditionWaitFailStrategy implements ConditionWaitFailStrategy {
    @Override
    public <T> void fail(@Nonnull final String reason, @Nonnull final ConditionFunction<T> function, @Nonnull final Matcher<? super T> matcher, @Nonnegative final long consumedMillis) {
      throw new RuntimeException("Exception raised just for testing.");
    }

    @Override
    public void fail(@Nonnull final String reason, @Nonnull final ConditionFunction<?> function, @Nonnull final ExpressionEvaluationException exception, @Nonnegative final long consumedMillis) {
      throw new RuntimeException("Exception raised just for testing.");
    }
  }

  private final class TestedConditionWaitImpl extends ConditionWaitImpl<Object> {

    TestedConditionWaitImpl(@Nullable final String message) {
      super(message, new TestedConditionFunction(), anything(), TIMEOUT_MILLIS, new TestedConditionWaitFailStrategy());
    }

    @Override
    protected long now() {
      return nextCall(ConditionWaitCall.NOW).asLong();
    }

    @Override
    protected void sleep(final long millis) {
      final long expected = nextCall(ConditionWaitCall.SLEEP).asLong();
      assertEquals("Expected number of milliseconds to wait.", expected, millis);
    }
  }

  private class TestedConditionFunction extends ConditionFunction<Object> {
    TestedConditionFunction() {
      super(new TestedExpression());
    }

    @Nonnull
    @Override
    public Boolean apply(@Nullable final Matcher<? super Object> matcher) {
      return nextCall(ConditionWaitCall.FUNCTION_RESULT).asBoolean();
    }
  }

  private ExpectedCall nextCall(@Nonnull final ConditionWaitCall type) {
    final ExpectedCall peek = expectedCalls.peek();
    if (peek == null) {
      fail(format("Call of type %s but did not expect any more calls.", type));
    }
    if (!type.equals(peek.getCall())) {
      fail(format("Call of type %s but expected %s.", type, peek.getCall()));
    }
    final ExpectedCall result = expectedCalls.poll();
    if (result.getResult() instanceof RuntimeException) {
      throw (RuntimeException) result.getResult();
    }
    return result;
  }

  private static final class TestedExpression implements Expression<Object> {
    @Override
    public void describeTo(final Description description) {
    }

    @Override
    public Object get() {
      return null;
    }
  }

  private final class ExpectedCallsBuilder {
    private final Collection<ExpectedCall> localExpectedCalls = new ArrayList<ExpectedCall>();

    public ExpectedCallsBuilder add(final ConditionWaitCall call, final Object result) {
      localExpectedCalls.add(new ExpectedCall(call, result));
      return this;
    }

    public LinkedList<ExpectedCall> build() {
      return new LinkedList<ExpectedCall>(localExpectedCalls);
    }
  }

  private static final class ExpectedCall {
    private final ConditionWaitCall call;
    private final Object result;

    private ExpectedCall(final ConditionWaitCall call, final Object result) {
      this.call = call;
      this.result = result;
    }

    public ConditionWaitCall getCall() {
      return call;
    }

    public Object getResult() {
      return result;
    }

    public long asLong() {
      return (Long) result;
    }

    public boolean asBoolean() {
      return (Boolean) result;
    }

  }

  private enum ConditionWaitCall {
    NOW,
    SLEEP,
    FUNCTION_RESULT
  }
}

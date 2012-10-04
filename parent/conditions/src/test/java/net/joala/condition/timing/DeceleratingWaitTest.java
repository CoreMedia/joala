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

package net.joala.condition.timing;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import net.joala.data.DataProvider;
import net.joala.data.DataProvidingException;
import net.joala.data.random.DefaultRandomStringProvider;
import net.joala.data.random.RandomDoubleProvider;
import net.joala.lab.junit.template.TestToString;
import net.joala.time.Timeout;
import net.joala.time.TimeoutImpl;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.round;
import static java.lang.String.format;
import static net.joala.lab.junit.template.TestToString.testToString;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;

/**
 * <p>
 * Tests {@link DeceleratingWait}. This test is a complete whitebox test knowing all calls to methods outside.
 * It expects a certain sequence of commands and the test defines all their expected results.
 * </p>
 *
 * @since 8/27/12
 */
@RunWith(MockitoJUnitRunner.class)
public class DeceleratingWaitTest {

  private static final double TEST_TIMEOUT_FACTOR = 5d;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() throws Exception {
    doThrow(mockFailException).when(mockWaitFailStrategy).fail(anyString(), any(), any(), any(), any(Matcher.class), anyLong());
    doThrow(mockFailException).when(mockWaitFailStrategy).fail(anyString(), any(), any(), any(Throwable.class), anyLong());
    timeout = new TimeoutImpl(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
  }

  @Test
  public void until_returns_immediately_if_first_evaluation_succeeds() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait();
  }

  @Test
  public void until_retries_once_and_then_succeeds() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, false)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.SLEEP, DeceleratingWait.INITIAL_DELAY)
            .add(WaitCall.NOW, DeceleratingWait.INITIAL_DELAY)
            .add(WaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait();
  }

  @SuppressWarnings("NewExceptionWithoutArguments")
  @Test
  public void until_retries_once_with_exception_and_then_succeeds() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, stateQueryException)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.SLEEP, DeceleratingWait.INITIAL_DELAY)
            .add(WaitCall.NOW, DeceleratingWait.INITIAL_DELAY)
            .add(WaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait();
  }

  @Test
  public void until_retries_until_timeout_and_fails() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, false)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * 2L)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * 2L)
            .build();
    assertFailedWait();
  }

  @Test
  public void until_retries_with_exceptions_until_timeout_and_fails() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, stateQueryException)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * 2L)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * 2L)
            .build();
    assertFailedWait();
  }

  @Test
  public void until_retries_with_exponential_backoff() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, false)
            .add(WaitCall.NOW, DeceleratingWait.INITIAL_DELAY)
            .add(WaitCall.SLEEP, DeceleratingWait.INITIAL_DELAY)
            .add(WaitCall.NOW, DeceleratingWait.INITIAL_DELAY)
            .add(WaitCall.FUNCTION_RESULT, false)
            .add(WaitCall.NOW, DeceleratingWait.INITIAL_DELAY)
            .add(WaitCall.SLEEP, round(DeceleratingWait.INITIAL_DELAY * DeceleratingWait.DECELERATION_FACTOR))
            .add(WaitCall.NOW, round(DeceleratingWait.INITIAL_DELAY * DeceleratingWait.DECELERATION_FACTOR) + DeceleratingWait.INITIAL_DELAY)
            .add(WaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait();
  }

  @Test
  public void until_polling_should_adopt_to_evaluation_duration() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L) // used to calculate deadline
            .add(WaitCall.NOW, 0L) // used to get timestamp before evaluation
            .add(WaitCall.FUNCTION_RESULT, false) // call function but fail
            .add(WaitCall.NOW, DeceleratingWait.INITIAL_DELAY * 2) // get time after evaluation; bigger than last (initial) delay
            .add(WaitCall.SLEEP, DeceleratingWait.INITIAL_DELAY * 2) // assertion: Now sleep interval should be equal to last duration
            .add(WaitCall.NOW, DeceleratingWait.INITIAL_DELAY * 2) // play it to the end
            .add(WaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait();
  }

  @SuppressWarnings("MagicNumber")
  @Test
  public void sleep_should_sleep_for_specified_millis() throws Exception {
    final long sleepMillis = 200L;
    final long before = System.currentTimeMillis();
    new DeceleratingWait(timeout, mockWaitFailStrategy).sleep(sleepMillis);
    final long after = System.currentTimeMillis();
    final long delay = after - before;
    assertThat("Wait enough time.", delay, greaterThan(sleepMillis / 2));
  }

  @Test
  public void now_should_return_system_time_millis() throws Exception {
    final long before = System.currentTimeMillis();
    final long now = new DeceleratingWait(timeout, mockWaitFailStrategy).nowMillis();
    final long after = System.currentTimeMillis();
    // Check loosely in case the clock drifts.
    assertTrue("now = " + now + " at " + before, before < now + 100);
    assertTrue("now = " + now + " at " + after, after > now - 100);
  }

  @Test
  public void toString_should_contain_necessary_information() throws Throwable {
    final DeceleratingWait wait = new DeceleratingWait(timeout, TIMEOUT_FACTOR_PROVIDER.get(), mockWaitFailStrategy);
    testToString(wait);
  }

  @Test
  public void constructor_noarg_should_correctly_initialize_wait_for_successful_run() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait(new TestedDeceleratingWait());
  }

  @Test
  public void constructor_noarg_should_correctly_initialize_wait_for_timeout_run() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, false)
            .add(WaitCall.NOW, DeceleratingWait.DEFAULT_TIMEOUT_MILLIS * 2L)
            .add(WaitCall.NOW, DeceleratingWait.DEFAULT_TIMEOUT_MILLIS * 2L)
            .build();
    assertFailedWait(new TestedDeceleratingWait());
  }

  @Test
  public void constructor_noarg_should_correctly_initialize_wait_for_timeout_on_exception_run() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, stateQueryException)
            .add(WaitCall.NOW, DeceleratingWait.DEFAULT_TIMEOUT_MILLIS * 2L)
            .add(WaitCall.NOW, DeceleratingWait.DEFAULT_TIMEOUT_MILLIS * 2L)
            .build();
    assertFailedWait(new TestedDeceleratingWait());
  }

  @Test
  public void constructor_timeoutArg_should_correctly_initialize_wait_for_successful_run() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait(new TestedDeceleratingWait(timeout));
  }

  @Test
  public void constructor_timeoutArg_should_correctly_initialize_wait_for_timeout_run() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, false)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * 2L)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * 2L)
            .build();
    assertFailedWait(new TestedDeceleratingWait(timeout));
  }

  @Test
  public void constructor_timeoutArg_should_correctly_initialize_wait_for_timeout_on_exception_run() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, stateQueryException)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * 2L)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * 2L)
            .build();
    assertFailedWait(new TestedDeceleratingWait(timeout));
  }

  @Test
  public void constructor_timeoutAndFactorArgs_should_correctly_initialize_wait_for_successful_run() throws Exception {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait(new TestedDeceleratingWait(timeout));
  }

  @Test
  public void constructor_timeoutAndFactorArgs_should_correctly_initialize_wait_for_timeout_run() throws Exception {
    final double timeoutFactor = TEST_TIMEOUT_FACTOR;
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, false)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * 2L)
            .add(WaitCall.SLEEP, TIMEOUT_MILLIS * 2L)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * 2L)
            .add(WaitCall.FUNCTION_RESULT, false)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * (timeoutFactor + 1))
            .build();
    assertFailedWait(new TestedDeceleratingWait(timeout, timeoutFactor));
  }

  @Test
  public void constructor_timeoutAndFactorArgs_should_correctly_initialize_wait_for_timeout_on_exception_run() throws Exception {
    final double timeoutFactor = TEST_TIMEOUT_FACTOR;
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, stateQueryException)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * 2L)
            .add(WaitCall.SLEEP, TIMEOUT_MILLIS * 2L)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * 2L)
            .add(WaitCall.FUNCTION_RESULT, stateQueryException)
            .add(WaitCall.NOW, TIMEOUT_MILLIS * (timeoutFactor + 1))
            .build();
    assertFailedWait(new TestedDeceleratingWait(timeout, timeoutFactor));
  }

  private static final long TIMEOUT_MILLIS = 500L;
  private static final DataProvider<Double> TIMEOUT_FACTOR_PROVIDER = new RandomDoubleProvider().min(0d).fixate();

  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private WaitFailStrategy mockWaitFailStrategy;
  private Timeout timeout;
  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private IgnorableStateQueryException stateQueryException;
  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private RuntimeException mockFailException;
  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private Object mockInput;
  private final Function<? super Object, Boolean> stateQuery = new StateQueryFunction();

  private LinkedList<ExpectedCall> expectedCalls;

  private void assertSuccessfulWait() throws DataProvidingException {
    assertSuccessfulWait(createWait());
  }

  private void assertSuccessfulWait(final TestedDeceleratingWait wait) throws DataProvidingException {
    wait.until();
    assertThat("All expected commands should have been called. Thus the list of expected commands should be empty.", expectedCalls, Matchers.empty());
  }

  private void assertFailedWait() throws DataProvidingException {
    assertFailedWait(createWait());
  }

  private void assertFailedWait(final TestedDeceleratingWait wait) throws DataProvidingException {
    try {
      wait.until();
      fail("Should have failed with timeout failure.");
    } catch (RuntimeException ignored) {
      // fine
    }
    assertThat("All expected commands should have been called. Thus the list of expected commands should be empty.", expectedCalls, Matchers.empty());
  }

  private final class TestedDeceleratingWait extends DeceleratingWait {
    private TestedDeceleratingWait() {
    }

    private TestedDeceleratingWait(@Nonnull final Timeout timeout) {
      super(timeout);
    }

    private TestedDeceleratingWait(@Nonnull final Timeout timeout, @Nonnull final WaitFailStrategy failStrategy) {
      super(timeout, failStrategy);
    }

    private TestedDeceleratingWait(@Nonnull final Timeout timeout, @Nonnegative final double timeoutFactor) {
      super(timeout, timeoutFactor);
    }

    public void until() throws DataProvidingException {
      this.until(new DefaultRandomStringProvider().get(), mockInput, stateQuery, Matchers.equalTo(Boolean.TRUE));
    }

    @Override
    protected long nowMillis() {
      return nextCall(WaitCall.NOW).asLong();
    }

    @Override
    protected void sleep(final long millis) {
      final ExpectedCall call = nextCall(WaitCall.SLEEP);
      final long expected = call.asLong();
      assertEquals(format("Expected number of milliseconds to wait (%s).", call), expected, millis);
    }

  }

  private TestedDeceleratingWait createWait() {
    return new TestedDeceleratingWait(timeout, mockWaitFailStrategy);
  }

  private class StateQueryFunction implements Function<Object, Boolean> {
    @Override
    public Boolean apply(@Nullable final Object input) {
      return nextCall(WaitCall.FUNCTION_RESULT).asBoolean();
    }
  }

  private ExpectedCall nextCall(@Nonnull final WaitCall type) {
    final ExpectedCall peek = expectedCalls.peek();
    if (peek == null) {
      fail(format("Call of type %s but did not expect any more calls.", type));
    }
    if (!type.equals(peek.getCall())) {
      fail(format("Call of type %s but expected %s (%s).", type, peek.getCall(), peek));
    }
    final ExpectedCall result = expectedCalls.poll();
    if (result.getResult() instanceof RuntimeException) {
      throw (RuntimeException) result.getResult();
    }
    return result;
  }

  private final class ExpectedCallsBuilder {
    private final Collection<ExpectedCall> localExpectedCalls = new ArrayList<ExpectedCall>();

    public ExpectedCallsBuilder add(final WaitCall call, final Object result) {
      localExpectedCalls.add(new ExpectedCall(localExpectedCalls.size(), call, result));
      return this;
    }

    public LinkedList<ExpectedCall> build() {
      return new LinkedList<ExpectedCall>(localExpectedCalls);
    }
  }

  private static final class ExpectedCall {
    private final int position;
    private final WaitCall call;
    private final Object result;

    private ExpectedCall(final int position, final WaitCall call, final Object result) {
      this.position = position;
      this.call = call;
      this.result = result;
    }

    public WaitCall getCall() {
      return call;
    }

    public Object getResult() {
      return result;
    }

    public long asLong() {
      return (Long) result;
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this)
              .add("position", position)
              .add("call", call)
              .add("result", result)
              .toString();
    }

    public Boolean asBoolean() {
      return (Boolean) result;
    }
  }

  private enum WaitCall {
    NOW,
    SLEEP,
    FUNCTION_RESULT
  }
}

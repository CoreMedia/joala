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

package net.joala.condition.timing;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import net.joala.time.Timeout;
import net.joala.time.TimeoutImpl;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.PrimitiveIterator.OfDouble;
import java.util.PrimitiveIterator.OfLong;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.lang.Math.round;
import static java.lang.String.format;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

/**
 * <p>
 * Tests {@link DeceleratingWait}. This test is a complete whitebox test knowing all calls to methods outside.
 * It expects a certain sequence of commands and the test defines all their expected results.
 * </p>
 *
 * @since 8/27/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
@RunWith(MockitoJUnitRunner.class)
public class DeceleratingWaitTest {

  private static final double TEST_TIMEOUT_FACTOR = 5d;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    doThrow(mockFailException).when(mockWaitFailStrategy).fail(anyString(), any(), any(), any(), any(Matcher.class), anyLong());
    doThrow(mockFailException).when(mockWaitFailStrategy).fail(anyString(), any(), any(), any(Throwable.class), anyLong());
    timeout = new TimeoutImpl(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
  }

  @Test
  public void until_returns_immediately_if_first_evaluation_succeeds() {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait();
  }

  @Test
  public void until_retries_once_and_then_succeeds() {
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
  public void until_retries_once_with_exception_and_then_succeeds() {
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
  public void until_retries_until_timeout_and_fails() {
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
  public void until_retries_with_exceptions_until_timeout_and_fails() {
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
  public void until_retries_with_exponential_backoff() {
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
  public void until_polling_should_adopt_to_evaluation_duration() {
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
  public void now_should_return_system_time_millis() {
    final long before = System.currentTimeMillis();
    final long now = new DeceleratingWait(timeout, mockWaitFailStrategy).nowMillis();
    final long after = System.currentTimeMillis();
    // Check loosely in case the clock drifts.
    assertTrue("now = " + now + " at " + before, before < now + 100);
    assertTrue("now = " + now + " at " + after, after > now - 100);
  }

  @Test
  public void toString_should_contain_necessary_information() { // NOSONAR: from JUnit
    final DeceleratingWait wait = new DeceleratingWait(timeout, TIMEOUT_FACTOR_PROVIDER.nextDouble(), mockWaitFailStrategy);
    assertThat(wait, Matchers.hasToString(Matchers.containsString(DeceleratingWait.class.getSimpleName())));
  }

  @Test
  public void constructor_noarg_should_correctly_initialize_wait_for_successful_run() {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait(new TestedDeceleratingWait());
  }

  @Test
  public void constructor_noarg_should_correctly_initialize_wait_for_timeout_run() {
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
  public void constructor_noarg_should_correctly_initialize_wait_for_timeout_on_exception_run() {
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
  public void constructor_timeoutArg_should_correctly_initialize_wait_for_successful_run() {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait(new TestedDeceleratingWait(timeout));
  }

  @Test
  public void constructor_timeoutArg_should_correctly_initialize_wait_for_timeout_run() {
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
  public void constructor_timeoutArg_should_correctly_initialize_wait_for_timeout_on_exception_run() {
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
  public void constructor_timeoutAndFactorArgs_should_correctly_initialize_wait_for_successful_run() {
    expectedCalls = new ExpectedCallsBuilder()
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.NOW, 0L)
            .add(WaitCall.FUNCTION_RESULT, true)
            .build();
    assertSuccessfulWait(new TestedDeceleratingWait(timeout));
  }

  @Test
  public void constructor_timeoutAndFactorArgs_should_correctly_initialize_wait_for_timeout_run() {
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
  public void constructor_timeoutAndFactorArgs_should_correctly_initialize_wait_for_timeout_on_exception_run() {
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
  private static final OfDouble TIMEOUT_FACTOR_PROVIDER = new Random(0L).doubles(0d, 10000d).iterator();
  private static final OfLong LONG_PROVIDER = new Random(0L).longs(0L, Long.MAX_VALUE).iterator();
  private static final Supplier<String> RANDOM_STRING_SUPPLIER = () -> format("rand%d", LONG_PROVIDER.next());

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

  private void assertSuccessfulWait() {
    assertSuccessfulWait(createWait());
  }

  private void assertSuccessfulWait(final TestedDeceleratingWait wait) {
    wait.until();
    assertThat("All expected commands should have been called. Thus the list of expected commands should be empty.", expectedCalls, Matchers.empty());
  }

  private void assertFailedWait() {
    assertFailedWait(createWait());
  }

  private void assertFailedWait(final TestedDeceleratingWait wait) {
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

    private TestedDeceleratingWait(@NonNull final Timeout timeout) {
      super(timeout);
    }

    private TestedDeceleratingWait(@NonNull final Timeout timeout, @NonNull final WaitFailStrategy failStrategy) {
      super(timeout, failStrategy);
    }

    private TestedDeceleratingWait(@NonNull final Timeout timeout, final double timeoutFactor) {
      super(timeout, timeoutFactor);
    }

    void until() {
      this.until(RANDOM_STRING_SUPPLIER.get(), mockInput, stateQuery, Matchers.equalTo(Boolean.TRUE));
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

  private ExpectedCall nextCall(@NonNull final WaitCall type) {
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

  private static final class ExpectedCallsBuilder {
    private final Collection<ExpectedCall> localExpectedCalls = new ArrayList<>();

    ExpectedCallsBuilder add(final WaitCall call, final Object result) {
      localExpectedCalls.add(new ExpectedCall(localExpectedCalls.size(), call, result));
      return this;
    }

    LinkedList<ExpectedCall> build() {
      return new LinkedList<>(localExpectedCalls);
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

    WaitCall getCall() {
      return call;
    }

    Object getResult() {
      return result;
    }

    long asLong() {
      return (Long) result;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
                        .add("position", position)
                        .add("call", call)
                        .add("result", result)
                        .toString();
    }

    Boolean asBoolean() {
      return (Boolean) result;
    }
  }

  private enum WaitCall {
    NOW,
    SLEEP,
    FUNCTION_RESULT
  }
}

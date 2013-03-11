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

package net.joala.testlet;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.ErrorCollector;
import org.junit.runner.JUnitCore;
import org.junit.runners.model.MultipleFailureException;

import java.util.List;
import java.util.concurrent.Callable;

import static java.lang.String.format;
import static net.joala.testlet.ToStringTestlet.toStringTestlet;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.typeCompatibleWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

/**
 * <p>
 * Tests {@link ExceptionCollectingRunListener}.
 * </p>
 *
 * @since 10/10/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
public class ExceptionCollectingRunListenerTest {
  @Rule
  public final ErrorCollector collector = new ErrorCollector();

  private static final String FAILURE_MESSAGE = "Expected Failure.";
  private JUnitCore jUnitCore;
  private ExceptionCollectingRunListener listener;

  @Before
  public void setUp() throws Exception {
    jUnitCore = new JUnitCore();
    listener = new ExceptionCollectingRunListener();
    jUnitCore.addListener(listener);
  }

  @Test
  public void should_pass_for_no_errors() throws Exception {
    jUnitCore.run(PassingTest.class);
    try {
      listener.assertNoFailures();
    } catch (Throwable throwable) {
      collector.checkThat("No exception should have been thrown.", throwable, nullValue());
    }
    validateToString();
  }

  @Test
  public void should_collect_assertion_error() throws Exception {
    jUnitCore.run(AssertionFailedTest.class);
    try {
      listener.assertNoFailures();
    } catch (Throwable throwable) {
      collector.checkThat("Should have forwarded collected assertion failure.", throwable.getClass(), typeCompatibleWith(AssertionError.class));
      collector.checkThat("Message should be the expected one.", throwable.getMessage(), equalTo(FAILURE_MESSAGE));
    }
    validateToString();
  }

  @SuppressWarnings({"ConstantConditions", "ThrowableResultOfMethodCallIgnored"})
  @Test
  public void should_collect_multiple_assertion_errors() throws Exception {
    jUnitCore.run(MultipleAssertionsFailedTest.class);
    try {
      listener.assertNoFailures();
    } catch (Throwable throwable) {
      collector.checkThat("Should have forwarded collected assertion failure.", throwable.getClass(), typeCompatibleWith(MultipleFailureException.class));
      final MultipleFailureException multipleFailureException = (MultipleFailureException) throwable;
      final List<Throwable> failures = multipleFailureException.getFailures();
      for (int i = 0; i < failures.size(); i++) {
        final Throwable embeddedFailure = failures.get(i);
        collector.checkThat(format("Should have collected assertion failure no. %d.", i + 1), embeddedFailure.getClass(), typeCompatibleWith(AssertionError.class));
        collector.checkThat(format("Message no. %d should be the expected one.", i + 1), embeddedFailure.getMessage(), equalTo(FAILURE_MESSAGE));
      }
    }
    validateToString();
  }

  @Test
  public void should_collect_assumption_failure() throws Exception {
    jUnitCore.run(AssumptionFailedTest.class);
    try {
      listener.assertNoFailures();
    } catch (Throwable throwable) {
      collector.checkThat("Should have forwarded collected assumption failure.", throwable.getClass(), typeCompatibleWith(AssumptionViolatedException.class));
    }
    validateToString();
  }

  @SuppressWarnings({"ConstantConditions", "ThrowableResultOfMethodCallIgnored"})
  @Test
  public void should_collect_multiple_assumption_failures() throws Exception {
    jUnitCore.run(MultipleAssumptionsFailedTest.class);
    try {
      listener.assertNoFailures();
    } catch (Throwable throwable) {
      collector.checkThat("Should have forwarded collected assertion failure.", throwable.getClass(), typeCompatibleWith(MultipleFailureException.class));
      final MultipleFailureException multipleFailureException = (MultipleFailureException) throwable;
      final List<Throwable> failures = multipleFailureException.getFailures();
      for (int i = 0; i < failures.size(); i++) {
        final Throwable embeddedFailure = failures.get(i);
        collector.checkThat(format("Should have collected assertion failure no. %d.", i + 1), embeddedFailure.getClass(), typeCompatibleWith(AssumptionViolatedException.class));
      }
    }
    validateToString();
  }

  private void validateToString() {
    collector.checkSucceeds(new CallToStringTestlet());
  }

  public static final class AssertionFailedTest {
    @Test
    public void will_fail() {
      fail(FAILURE_MESSAGE);
    }
  }

  public static final class PassingTest {
    @Test
    public void will_pass() {
    }
  }

  public static final class MultipleAssertionsFailedTest {
    @Test
    public void will_fail1() {
      fail(FAILURE_MESSAGE);
    }

    @Test
    public void will_fail2() {
      fail(FAILURE_MESSAGE);
    }
  }

  public static final class AssumptionFailedTest {
    @Test
    public void will_fail_assumption() {
      assumeTrue(false);
    }
  }

  public static final class MultipleAssumptionsFailedTest {
    @Test
    public void will_fail_assumption1() {
      assumeTrue(false);
    }

    @Test
    public void will_fail_assumption2() {
      assumeTrue(false);
    }
  }

  private class CallToStringTestlet implements Callable<Object> {
    @Override
    public Object call() throws Exception {
      try {
        toStringTestlet(listener).run();
      } catch (Throwable throwable) {
        assertThat("toString should have passed.", throwable, nullValue());
      }
      return null;
    }
  }
}

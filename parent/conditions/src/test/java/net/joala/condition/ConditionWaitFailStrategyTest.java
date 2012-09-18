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

import net.joala.base.ConditionWaitFailStrategy;
import org.hamcrest.Matchers;
import org.junit.Test;

import static java.lang.String.format;
import static net.joala.condition.RandomData.randomString;
import static net.joala.matcher.exception.CausedBy.causedBy;
import static net.joala.matcher.exception.MessageContains.messageContains;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * <p>
 * Base test for classes implementing {@link net.joala.base.ConditionWaitFailStrategy}.
 * </p>
 *
 * @since 8/26/12
 */
public abstract class ConditionWaitFailStrategyTest<S extends ConditionWaitFailStrategy, T extends Throwable> {
  @Test
  public void failed_match_method_should_throw_exception() throws Exception {
    final S strategy = getFailStrategy();
    boolean success = false;
    try {
      strategy.fail(randomString(), new StringConditionFunction(randomString()), nullValue(), 200l);
      success = true;
    } catch (RuntimeException ignored) {
      // fine, that's what we expected
    } catch (Error ignored) {
      // fine that's what we expected
    } catch (Throwable t) {
      fail(format("Unexpected exception of type %s thrown.", t.getClass()));
    }
    assertFalse("An exception should have been thrown.", success);
  }

  @Test
  public void failed_with_exceptions_method_should_throw_exception() throws Exception {
    final S strategy = getFailStrategy();
    boolean success = false;
    try {
      strategy.fail(randomString(), new StringConditionFunction(randomString()), new ExpressionEvaluationException(randomString()), 200l);
      success = true;
    } catch (RuntimeException ignored) {
      // fine, that's what we expected
    } catch (Error ignored) {
      // fine that's what we expected
    } catch (Throwable t) {
      fail(format("Unexpected exception of type %s thrown.", t.getClass()));
    }
    assertFalse("An exception should have been thrown.", success);
  }

  @Test
  public void message_on_failed_match_should_be_contained_in_exception() throws Throwable {
    final S strategy = getFailStrategy();
    final String message = randomString("message");
    boolean success = false;
    try {
      strategy.fail(message, new StringConditionFunction(randomString("expressionValue")), nullValue(), 200l);
      success = true;
    } catch (Throwable t) {
      if (!getRaisedExceptionType().isAssignableFrom(t.getClass())) {
        throw t;
      }
      assertThat("Message should be contained in exception message.", t.getMessage(), containsString(message));
    }
    assertFalse("An exception should have been thrown.", success);
  }

  @Test
  public void message_when_failed_with_exception_should_be_contained_in_exception() throws Throwable {
    final S strategy = getFailStrategy();
    boolean success = false;
    final String message = randomString("message");
    try {
      strategy.fail(message, new StringConditionFunction(randomString("expressionValue")), new ExpressionEvaluationException(randomString("exceptionMessage")), 200l);
      success = true;
    } catch (Throwable t) {
      if (!getRaisedExceptionType().isAssignableFrom(t.getClass())) {
        throw t;
      }
      assertThat("Message should be contained in exception message.", t.getMessage(), containsString(message));
    }
    assertFalse("An exception should have been thrown.", success);
  }

  @Test
  public void cause_when_failed_with_exception_should_be_contained_in_exception() throws Throwable {
    final S strategy = getFailStrategy();
    boolean success = false;
    final String evaluationExceptionMessage = randomString();
    final ExpressionEvaluationException exception = new ExpressionEvaluationException(evaluationExceptionMessage);
    try {
      strategy.fail(randomString(), new StringConditionFunction(randomString()), exception, 200l);
      success = true;
    } catch (Throwable t) {
      if (!getRaisedExceptionType().isAssignableFrom(t.getClass())) {
        throw t;
      }
      assertThat("Cause should be mentioned in failure.", t,
              anyOf(
                      causedBy(exception),
                      messageContains(exception.getMessage(), true)
              )
      );
    }
    assertFalse("An exception should have been thrown.", success);
  }

  @Test
  public void should_throw_expected_exception_type_on_failure_because_of_exception() throws Exception {
    final S strategy = getFailStrategy();
    final ExpressionEvaluationException exception = new ExpressionEvaluationException(randomString());
    try {
      strategy.fail(randomString(), new StringConditionFunction(randomString()), exception, 200l);
      fail("Exception should have been thrown.");
    } catch (Throwable t) {
      assertThat("Raised exception should be of expected type.", t, Matchers.instanceOf(getRaisedExceptionType()));
    }
  }

  @Test
  public void should_throw_expected_exception_type_on_failure_because_mismatch() throws Exception {
    final S strategy = getFailStrategy();
    try {
      strategy.fail(randomString(), new StringConditionFunction(randomString()), nullValue(), 200l);
      fail("Exception should have been thrown.");
    } catch (Throwable t) {
      assertThat("Raised exception should be of expected type.", t, Matchers.instanceOf(getRaisedExceptionType()));
    }
  }


  protected abstract Class<T> getRaisedExceptionType();

  protected abstract S getFailStrategy();

  private static final class StringExpression extends AbstractExpression<String> {
    private final String str;

    private StringExpression(final String str) {
      super(format("StringExpression(%s)", str));
      this.str = str;
    }

    @Override
    public String get() {
      return str;
    }
  }

  private static final class StringConditionFunction extends ConditionFunction<String> {
    private final String str;

    StringConditionFunction(final String str) {
      super(new StringExpression(str));
      this.str = str;
    }

    @Override
    public String getCached() {
      return str;
    }
  }

}

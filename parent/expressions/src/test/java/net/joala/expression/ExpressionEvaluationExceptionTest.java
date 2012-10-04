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

package net.joala.expression;

import net.joala.data.DataProvider;
import net.joala.data.random.DefaultRandomStringProvider;
import net.joala.expression.ExpressionEvaluationException;
import org.junit.Test;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link net.joala.expression.ExpressionEvaluationException}.
 *
 * @since 8/25/12
 */
public class ExpressionEvaluationExceptionTest {
  private static final DataProvider<String> MESSAGE_PROVIDER = new DefaultRandomStringProvider().prefix("message").fixate();
  private static final DataProvider<String> MESSAGE_CAUSE_PROVIDER = new DefaultRandomStringProvider().prefix("messageCause").fixate();

  @SuppressWarnings("NewExceptionWithoutArguments")
  @Test
  public void default_constructor_should_work() throws Exception {
    try {
      throw new ExpressionEvaluationException();
    } catch (ExpressionEvaluationException e) {
      assertNull("Message is empty.", e.getMessage());
      assertNull("Cause is empty.", e.getCause());
    }
  }

  @Test
  public void constructor_with_message_should_work() throws Exception {
    final String message = MESSAGE_PROVIDER.get();
    try {
      throw new ExpressionEvaluationException(message);
    } catch (ExpressionEvaluationException e) {
      assertEquals("Message should be set correctly.", message, e.getMessage());
      assertNull("Cause not set.", e.getCause());
    }
  }

  @Test
  public void constructor_with_cause_should_work() throws Exception {
    final String message = MESSAGE_PROVIDER.get();
    final Exception cause = new Exception(message);
    try {
      throw new ExpressionEvaluationException(cause);
    } catch (ExpressionEvaluationException e) {
      assertSame("Cause should be set as expected.", cause, e.getCause());
      assertThat("Message should be derived from cause (default exception behavior).", e.getMessage(), containsString(message));
    }
  }

  @Test
  public void constructor_with_message_and_cause_should_work() throws Exception {
    final String message = MESSAGE_PROVIDER.get();
    final Exception cause = new Exception(MESSAGE_CAUSE_PROVIDER.get());
    try {
      throw new ExpressionEvaluationException(message, cause);
    } catch (ExpressionEvaluationException e) {
      assertSame("Cause should be set as expected.", cause, e.getCause());
      assertThat("Message should be set as expected.", e.getMessage(), containsString(message));
    }
  }
}

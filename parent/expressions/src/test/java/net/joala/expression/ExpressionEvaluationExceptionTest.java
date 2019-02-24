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

package net.joala.expression;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.function.Supplier;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link ExpressionEvaluationException}.
 *
 * @since 8/25/12
 */
public class ExpressionEvaluationExceptionTest {
  private static final Supplier<String> MESSAGE_PROVIDER = () -> "message_" + RandomStringUtils.random(20);
  private static final Supplier<String> MESSAGE_CAUSE_PROVIDER = () -> "messageCause_" + RandomStringUtils.random(20);

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

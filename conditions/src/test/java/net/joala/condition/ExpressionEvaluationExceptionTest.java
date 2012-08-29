package net.joala.condition;

import org.junit.Test;

import static net.joala.condition.RandomData.randomString;
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
    final String message = randomString();
    try {
      throw new ExpressionEvaluationException(message);
    } catch (ExpressionEvaluationException e) {
      assertEquals("Message should be set correctly.", message, e.getMessage());
      assertNull("Cause not set.", e.getCause());
    }
  }

  @Test
  public void constructor_with_cause_should_work() throws Exception {
    final String message = randomString();
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
    final String message = randomString();
    final Exception cause = new Exception(randomString());
    try {
      throw new ExpressionEvaluationException(message, cause);
    } catch (ExpressionEvaluationException e) {
      assertSame("Cause should be set as expected.", cause, e.getCause());
      assertThat("Message should be set as expected.", e.getMessage(), containsString(message));
    }
  }
}

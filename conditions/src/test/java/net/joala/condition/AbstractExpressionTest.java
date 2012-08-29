package net.joala.condition;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static net.joala.condition.RandomData.randomString;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Tests {@link AbstractExpression}.
 * </p>
 *
 * @since 8/26/12
 */
public class AbstractExpressionTest {
  @Test
  public void empty_simple_description_should_not_cause_update_of_description() throws Exception {
    final SelfDescribing expression = new AbstractExpression<Object>() {
      @Override
      public Object get() {
        return null;
      }
    };
    final String text = randomString();
    final Description description = new StringDescription().appendText(text);
    expression.describeTo(description);
    assertEquals("Description should not have been updated.", text, description.toString());
  }

  @Test
  public void non_empty_simple_description_should_be_added_to_description() throws Exception {
    final String simpleDescription = randomString();
    final SelfDescribing expression = new AbstractExpression<Object>(simpleDescription) {
      @Override
      public Object get() {
        return null;
      }
    };
    final String text = randomString();
    final Description description = new StringDescription().appendText(text);
    expression.describeTo(description);
    assertThat("Original description should still be contained.", description.toString(), containsString(text));
    assertThat("Description should be enriched by expression's description.", description.toString(), containsString(simpleDescription));
  }

  @Test
  public void expression_description_should_be_contained_in_toString() throws Exception {
    final String simpleDescription = randomString();
    final SelfDescribing expression = new AbstractExpression<Object>(simpleDescription) {
      @Override
      public Object get() {
        return null;
      }
    };
    assertThat("toString should contain description.", expression.toString(), containsString(simpleDescription));
  }
}

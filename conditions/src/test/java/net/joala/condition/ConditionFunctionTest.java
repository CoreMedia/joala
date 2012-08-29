package net.joala.condition;

import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.SelfDescribing;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import static net.joala.condition.RandomData.randomString;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @since 8/27/12
 */
public class ConditionFunctionTest {
  @Test
  public void apply_should_accept_any_if_matcher_is_null() throws Exception {
    final Object value = randomString();
    final ObjectExpression expression = mock(ObjectExpression.class);
    when(expression.get()).thenReturn(value);
    final ConditionFunction<Object> function = new ConditionFunction<Object>(expression);
    assertTrue("Given no matcher any returned value from expression should be fine.",
            function.apply(null));
  }

  @Test
  public void apply_should_accept_any_if_matcher_matches_any() throws Exception {
    final Object value = randomString();
    final ObjectExpression expression = mock(ObjectExpression.class);
    when(expression.get()).thenReturn(value);
    final ConditionFunction<Object> function = new ConditionFunction<Object>(expression);
    assertTrue("Given matcher matching any returned value from expression should be fine.",
            function.apply(anything()));
  }

  @Test
  public void apply_should_accept_when_matcher_matches() throws Exception {
    final String value = randomString();
    final StringExpression expression = mock(StringExpression.class);
    when(expression.get()).thenReturn(value);
    final ConditionFunction<String> function = new ConditionFunction<String>(expression);
    assertTrue("If matcher matches apply should return true.",
            function.apply(IsEqual.<String>equalTo(value)));
  }

  @Test
  public void apply_should_deny_when_matcher_does_not_match() throws Exception {
    final String value = randomString();
    final StringExpression expression = mock(StringExpression.class);
    when(expression.get()).thenReturn(value);
    final ConditionFunction<String> function = new ConditionFunction<String>(expression);
    assertFalse("If matcher does not matche apply should return false.",
            function.apply(nullValue()));
  }

  @Test
  public void cache_last_value_on_successful_matching_expression_evaluation() throws Exception {
    final Object value = randomString();
    final ObjectExpression expression = mock(ObjectExpression.class);
    when(expression.get()).thenReturn(value);
    final ConditionFunction<Object> function = new ConditionFunction<Object>(expression);
    assumeTrue(function.apply(anything()));
    assertEquals("Cached value should be the last value returned.", value, function.getCached());
  }

  @Test
  public void cache_last_value_on_successful_non_matching_expression_evaluation() throws Exception {
    final Object value = randomString();
    final ObjectExpression expression = mock(ObjectExpression.class);
    when(expression.get()).thenReturn(value);
    final ConditionFunction<Object> function = new ConditionFunction<Object>(expression);
    assumeThat(function.apply(nullValue()), Matchers.equalTo(Boolean.FALSE));
    assertEquals("Cached value should be the last value returned even if it did not match.", value, function.getCached());
  }

  @Test
  public void expression_description_should_be_added_to_description() throws Exception {
    final ObjectExpression expression = mock(ObjectExpression.class);
    final Description description = mock(Description.class);
    final SelfDescribing function = new ConditionFunction<Object>(expression);

    function.describeTo(description);

    verify(description, times(1)).appendDescriptionOf(expression);
  }

  @Test
  public void toString_should_contain_reference_to_expression() throws Exception {
    final ObjectExpression expression = mock(ObjectExpression.class);
    final ConditionFunction<Object> function = new ConditionFunction<Object>(expression);
    assertThat("toString should contain reference to expression.", function.toString(), containsString(expression.toString()));
  }

  private interface ObjectExpression extends Expression<Object> {
  }

  private interface StringExpression extends Expression<String> {
  }

}

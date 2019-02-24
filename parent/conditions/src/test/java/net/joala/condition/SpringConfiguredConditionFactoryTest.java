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

package net.joala.condition;

import net.joala.expression.Expression;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.function.Supplier;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Tests example for injectable condition-factory.
 * </p>
 *
 * @since 9/3/12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/joala/condition/test-context.xml")
public class SpringConfiguredConditionFactoryTest {
  private static final Supplier<String> EXPRESSION_VALUE_PROVIDER = () -> "expressionValue_" + RandomStringUtils.random(20);

  @Inject
  private ConditionFactory conditionFactory;

  @Test
  public void condition_factory_should_have_been_injected() throws Exception {
    assertNotNull("Condition factory should not be null.", conditionFactory);
    assertThat("Condition factory should match expected default.",
            conditionFactory,
            instanceOf(DefaultConditionFactory.class));
  }

  @Test
  public void instantiating_boolean_condition_should_be_possible() throws Exception {
    final BooleanTestExpression expression = mock(BooleanTestExpression.class);
    when(expression.get()).thenReturn(Boolean.TRUE);
    final BooleanCondition condition = conditionFactory.booleanCondition(expression);
    assertNotNull("Retrieved boolean condition should not be null.", condition);
    assertTrue("Condition should evaluate expression to true.", condition.await());
  }

  @Test
  public void instantiating_string_condition_should_be_possible() throws Exception {
    final StringTestExpression expression = mock(StringTestExpression.class);
    final String value = EXPRESSION_VALUE_PROVIDER.get();
    when(expression.get()).thenReturn(value);
    final Condition<String> condition = conditionFactory.condition(expression);
    assertNotNull("Retrieved string condition should not be null.", condition);
    assertEquals("Result of expression should be expected string.", value, condition.await());
  }


  private interface BooleanTestExpression extends Expression<Boolean> {
  }

  private interface StringTestExpression extends Expression<String> {
  }
}

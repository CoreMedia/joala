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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static net.joala.condition.RandomData.randomString;
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
    final String value = randomString("expressionValue");
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

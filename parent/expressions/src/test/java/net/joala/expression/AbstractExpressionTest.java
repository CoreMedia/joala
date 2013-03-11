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

import net.joala.data.DataProvider;
import net.joala.data.random.DefaultRandomStringProvider;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static net.joala.testlet.ToStringTestlet.toStringTestlet;
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
@SuppressWarnings("ProhibitedExceptionDeclared")
public class AbstractExpressionTest {
  private static final DataProvider<String> TEXT_PROVIDER = new DefaultRandomStringProvider().prefix("text").fixate();
  private static final DataProvider<String> DESCRIPTION_PROVIDER = new DefaultRandomStringProvider().prefix("description").fixate();

  @Test
  public void empty_simple_description_should_not_cause_update_of_description() throws Exception {
    final SelfDescribing expression = new MockExpression();
    final String text = TEXT_PROVIDER.get();
    final Description description = new StringDescription().appendText(text);
    expression.describeTo(description);
    assertEquals("Description should not have been updated.", text, description.toString());
  }

  @Test
  public void non_empty_simple_description_should_be_added_to_description() throws Exception {
    final String simpleDescription = DESCRIPTION_PROVIDER.get();
    final SelfDescribing expression = new MockExpression(simpleDescription);
    final String text = TEXT_PROVIDER.get();
    final Description description = new StringDescription().appendText(text);
    expression.describeTo(description);
    assertThat("Original description should still be contained.", description.toString(), containsString(text));
    assertThat("Description should be enriched by expression's description.", description.toString(), containsString(simpleDescription));
  }

  @SuppressWarnings("ProhibitedExceptionDeclared")
  @Test
  public void toString_should_contain_necessary_information() throws Throwable { // NOSONAR: Adopted from JUnit standard
    final AbstractExpression<?> expression = new MockExpression(DESCRIPTION_PROVIDER.get());
    toStringTestlet(expression).fieldsFromClass(AbstractExpression.class).run();
  }

  private static class MockExpression extends AbstractExpression<Object> {
    private MockExpression() {
    }

    private MockExpression(final String simpleDescription) {
      super(simpleDescription);
    }

    @Override
    public Object get() {
      return null;
    }
  }
}

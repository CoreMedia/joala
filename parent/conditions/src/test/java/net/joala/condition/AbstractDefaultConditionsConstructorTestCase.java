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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.condition;

import net.joala.bdd.reference.Reference;
import net.joala.bdd.testcase.BddTestCase;
import net.joala.condition.timing.WaitFactory;
import net.joala.expression.Expression;
import net.joala.time.Timeout;
import org.junit.Test;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static net.joala.bdd.reference.References.ref;
import static net.joala.condition.DefaultConditionCommonSteps.GivenParameterValue.givenParameter;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link DefaultCondition} and related.
 *
 * @since 0.8.0
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
public abstract class AbstractDefaultConditionsConstructorTestCase<T extends DefaultCondition> extends BddTestCase {

  private final Class<T> conditionClass;

  protected AbstractDefaultConditionsConstructorTestCase(final Class<T> conditionClass) {
    this.conditionClass = conditionClass;
  }

  @PostConstruct
  private void initSteps() {
    _.setConditionClass(conditionClass);
  }

  @Test
  public void scenario_constructors_accept_nonnull_expression() throws Exception {
    subScenario_constructors_accept_nonnull_for_type(Expression.class);
  }

  @Test
  public void scenario_constructors_deny_null_expression() throws Exception {
    subScenario_constructors_deny_null_for_type(Expression.class);
  }

  @Test
  public void scenario_constructors_should_accept_nonnull_timeout() throws Exception {
    subScenario_constructors_accept_nonnull_for_type(Timeout.class);
  }

  @Test
  public void scenario_constructors_deny_null_timeout() throws Exception {
    subScenario_constructors_deny_null_for_type(Timeout.class);
  }

  @Test
  public void scenario_constructors_should_accept_nonnull_waitfactory() throws Exception {
    subScenario_constructors_accept_nonnull_for_type(WaitFactory.class);
  }

  @Test
  public void scenario_constructors_deny_null_waitfactory() throws Exception {
    subScenario_constructors_deny_null_for_type(WaitFactory.class);
  }

  private void subScenario_constructors_accept_nonnull_for_type(final Class<?> parameterType) {
    final Reference<Iterable<Throwable>> throwablesReference = ref("caught");
    final Reference<Iterable<? extends Constructor<?>>> constructorsReference = ref("C");

    _.given_constructors_C_for_conditions_with_argument_of_type_$1(constructorsReference, parameterType);
    _.when_I_call_constructors_C_with_nonnull_value_for_parameter_types_of_$2(constructorsReference, throwablesReference, parameterType);
    _.then_no_exceptions_are_thrown(throwablesReference);
  }

  private void subScenario_constructors_deny_null_for_type(final Class<?> parameterType) {
    final Reference<Iterable<Throwable>> throwablesReference = ref("caught");
    final Reference<Iterable<? extends Constructor<?>>> constructorsReference = ref("C");

    _.given_constructors_C_for_conditions_with_argument_of_type_$1(constructorsReference, parameterType);
    _.when_I_call_constructors_C_with_null_value_for_parameter_types_of_$2(constructorsReference, throwablesReference, parameterType);
    _.then_exceptions_are_thrown(throwablesReference);
  }

  @Inject
  private Steps _;

  @Named
  @Singleton
  public static class Steps extends DefaultConditionCommonSteps {
    public void when_I_call_constructors_C_with_nonnull_value_for_parameter_types_of_$2(
            final Reference<Iterable<? extends Constructor<?>>> constructorsReference,
            final Reference<Iterable<Throwable>> throwablesReference,
            final Class<?> givenParameterType) {
      final GivenParameterValue<?> givenExpression = givenParameter(givenParameterType);
      final Iterable<? extends Constructor<?>> constructors = constructorsReference.getNonNull();
      final List<Throwable> throwables = newArrayList();
      for (final Constructor<?> constructor : constructors) {
        throwables.add(tryConstruct(constructor, getParamValues(constructor, givenExpression)));
      }
      throwablesReference.set(throwables);
    }

    public void when_I_call_constructors_C_with_null_value_for_parameter_types_of_$2(
            final Reference<Iterable<? extends Constructor<?>>> constructorsReference,
            final Reference<Iterable<Throwable>> throwablesReference,
            final Class<?> givenParameterType) {
      final GivenParameterValue<?> givenExpression = givenParameter(givenParameterType, null);
      final Iterable<? extends Constructor<?>> constructors = constructorsReference.getNonNull();
      final List<Throwable> throwables = newArrayList();
      for (final Constructor<?> constructor : constructors) {
        throwables.add(tryConstruct(constructor, getParamValues(constructor, givenExpression)));
      }
      throwablesReference.set(throwables);
    }

    public void then_no_exceptions_are_thrown(final Reference<Iterable<Throwable>> throwablesReference) {
      assertThat(throwablesReference.get(), everyItem(nullValue(Throwable.class)));
    }

    public void then_exceptions_are_thrown(final Reference<Iterable<Throwable>> throwablesReference) {
      assertThat(throwablesReference.get(), everyItem(notNullValue(Throwable.class)));
    }

  }
}
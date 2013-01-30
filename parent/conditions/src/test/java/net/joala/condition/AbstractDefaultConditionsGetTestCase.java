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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static net.joala.bdd.reference.References.ref;
import static net.joala.condition.DefaultConditionCommonSteps.GivenParameterValue.givenParameter;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link net.joala.condition.DefaultCondition} and related.
 *
 * @since 0.8.0
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
public abstract class AbstractDefaultConditionsGetTestCase<T extends DefaultCondition> extends BddTestCase {

  private final Class<T> conditionClass;

  protected AbstractDefaultConditionsGetTestCase(final Class<T> conditionClass) {
    this.conditionClass = conditionClass;
  }

  @PostConstruct
  private void initSteps() {
    _.setConditionClass(conditionClass);
  }

  @Test
  public void scenario_exception_set_on_creation_is_used_for_evaluation() throws Exception {
    final Reference<Iterable<? extends Constructor<?>>> constructorsReference = ref("C");
    final Reference<Iterable<? extends T>> instancesReference = ref("CD");
    final Reference<Iterable<? extends Expression>> expressionsReference = ref("E");

    _.given_constructors_C_for_conditions_with_argument_of_type_$1(constructorsReference, Expression.class);
    _.when_I_create_conditions_CD_with_constructors_C_and_expressions_E(instancesReference, constructorsReference, expressionsReference);
    _.then_calling_method_get_on_conditions_CD_uses_their_expressions_E(instancesReference, expressionsReference);
  }

  @Inject
  private Steps _;

  @Named
  @Singleton
  public static class Steps extends DefaultConditionCommonSteps {
    public void when_I_call_constructors_C_with_nonnull_value_for_parameter_types_of_$2(final Reference<Iterable<? extends Constructor<?>>> constructorsReference, final Reference<Iterable<Throwable>> throwablesReference, final Class<?> givenParameterType) {
      final GivenParameterValue<?> givenExpression = givenParameter(givenParameterType);
      final Iterable<? extends Constructor<?>> constructors = constructorsReference.getNonNull();
      final List<Throwable> throwables = newArrayList();
      for (final Constructor<?> constructor : constructors) {
        throwables.add(tryConstruct(constructor, getParamValues(constructor, givenExpression)));
      }
      throwablesReference.set(throwables);
    }

    public void when_I_create_conditions_CD_with_constructors_C_and_expressions_E(
            final Reference<Iterable<? extends DefaultCondition>> instancesReference, final Reference<Iterable<? extends Constructor<?>>> constructorsReference, final Reference<Iterable<? extends Expression>> expressionsReference) {
    }
  }
}
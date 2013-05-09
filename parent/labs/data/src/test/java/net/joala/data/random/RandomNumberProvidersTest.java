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

package net.joala.data.random;

import net.joala.internal.data.random.AbstractRandomNumberProvider;
import net.joala.internal.data.random.RandomDoubleProvider;
import net.joala.internal.data.random.RandomFloatProvider;
import net.joala.internal.data.random.RandomIntegerProvider;
import net.joala.internal.data.random.RandomLongProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.Random;

import static net.joala.internal.junit.ParameterizedParametersBuilders.singletonParametersBuilder;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Tests {@link net.joala.internal.data.random.RandomNumberProvider} and its implementations.
 * </p>
 *
 * @since 9/14/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
@RunWith(Parameterized.class)
public class RandomNumberProvidersTest<T extends Comparable<? extends Number>> {
  private static final Random GENERATOR = new Random(System.currentTimeMillis());

  private final AbstractRandomNumberProvider<T> randomNumberProvider;

  public RandomNumberProvidersTest(final AbstractRandomNumberProvider<T> randomNumberProvider) {
    this.randomNumberProvider = spy(randomNumberProvider);
  }

  @Test
  public void random_should_be_able_to_return_lower_bound() throws Exception {
    when(randomNumberProvider.nextRandomRatio()).thenReturn(0d);
    final T random = randomNumberProvider.get();
    assertEquals("Returned random number be equal to MIN_VALUE.", randomNumberProvider.getNumberType().min(), random);
  }

  @Test
  public void random_should_be_able_to_return_upper_bound() throws Exception {
    when(randomNumberProvider.nextRandomRatio()).thenReturn(1d);
    final T random = randomNumberProvider.get();
    assertEquals("Returned random number be equal to MAX_VALUE.", randomNumberProvider.getNumberType().max(), random);
  }

  @Test
  public void random_should_respect_closed_lower_bound() throws Exception {
    when(randomNumberProvider.nextRandomRatio()).thenReturn(0d);
    final T lowerBound = randomNumberProvider.getNumberType().percentOf(GENERATOR.nextDouble(), randomNumberProvider.getNumberType().min());
    final T random = randomNumberProvider.min(lowerBound).get();
    assertEquals("Returned random number be equal to lower bound.", lowerBound, random);
  }

  @Test
  public void random_should_respect_closed_upper_bound() throws Exception {
    when(randomNumberProvider.nextRandomRatio()).thenReturn(1d);
    final T upperBound = randomNumberProvider.getNumberType().percentOf(GENERATOR.nextDouble(), randomNumberProvider.getNumberType().max());
    final T random = randomNumberProvider.max(upperBound).get();
    assertEquals("Returned random number be equal to upper bound.", upperBound, random);
  }

  @Test
  public void toString_should_contain_number_type() throws Exception {
    final String str = randomNumberProvider.toString();
    final Class<T> type = randomNumberProvider.getNumberType().getType();
    assertThat("Number type should be contained in toString.", str, containsString(String.valueOf(type)));
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return singletonParametersBuilder(RandomNumberProvidersTest.class)
            .add(new RandomDoubleProvider(),
                    new RandomFloatProvider(),
                    new RandomIntegerProvider(),
                    new RandomLongProvider())
            .build();
  }
}

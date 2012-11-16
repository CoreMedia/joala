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

package net.joala.newdata.random.impl;

import net.joala.newdata.random.AbstractRandomNumberProvider;
import net.joala.newdata.random.AbstractRandomNumberProviderBuilder;
import net.joala.newdata.random.FluentNumberRange;
import net.joala.newdata.random.RandomProvider;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.powermock.reflect.Whitebox;

import javax.inject.Provider;
import java.util.Collection;
import java.util.Random;

import static net.joala.lab.junit.ParameterizedParametersBuilders.defaultParametersBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * <p>
 * Tests {@link AbstractRandomNumberProvider} and its implementations.
 * </p>
 *
 * @since 9/14/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
@RunWith(Parameterized.class)
public class RandomNumberProviderImplsTest<T extends Number> {
  private final AbstractRandomNumberProvider<T> randomNumberProvider;
  private final T someValidNumberWithinRange;
  private final Provider<Random> randomProvider;
  private Random randomGenerator;

  public RandomNumberProviderImplsTest(final Provider<Random> randomProvider,
                                       final AbstractRandomNumberProvider<T> randomNumberProvider,
                                       final T someValidNumberWithinRange) {
    this.randomProvider = randomProvider;
    this.randomNumberProvider = randomNumberProvider;
    this.someValidNumberWithinRange = someValidNumberWithinRange;
  }

  @Before
  public void setUp() throws Exception {
    this.randomGenerator = mock(Random.class);
    when(this.randomProvider.get()).thenReturn(randomGenerator);
  }

  @Test
  public void random_should_be_able_to_return_lower_bound() throws Exception {
    when(randomGenerator.nextDouble()).thenReturn(0d);
    final T random = randomNumberProvider.get();
    final Object minDefault = Whitebox.invokeMethod(randomNumberProvider, "getMinDefault");
    assertEquals("Returned random number be equal to MIN_VALUE.", minDefault, random);
  }

  @Test
  public void random_should_be_able_to_return_upper_bound() throws Exception {
    when(randomGenerator.nextDouble()).thenReturn(1d);
    final T random = randomNumberProvider.get();
    final Object maxDefault = Whitebox.invokeMethod(randomNumberProvider, "getMaxDefault");
    assertEquals("Returned random number be equal to MAX_VALUE.", maxDefault, random);
  }

  @SuppressWarnings("TypeMayBeWeakened")
  @Test
  public void random_should_respect_lower_bound() throws Exception {
    when(randomGenerator.nextDouble()).thenReturn(0d);
    final FluentNumberRange<T> minResult = randomNumberProvider.min(someValidNumberWithinRange);
    assumeThat(minResult, Matchers.instanceOf(AbstractRandomNumberProviderBuilder.class));
    final AbstractRandomNumberProviderBuilder<T> providerBuilder = (AbstractRandomNumberProviderBuilder<T>) minResult;
    final T random = providerBuilder.build().get();
    assertEquals("Returned random number be equal to lower bound.", someValidNumberWithinRange, random);
  }

  @SuppressWarnings("TypeMayBeWeakened")
  @Test
  public void random_should_respect_closed_upper_bound() throws Exception {
    when(randomGenerator.nextDouble()).thenReturn(1d);
    final FluentNumberRange<T> maxResult = randomNumberProvider.max(someValidNumberWithinRange);
    assumeThat(maxResult, Matchers.instanceOf(AbstractRandomNumberProviderBuilder.class));
    final AbstractRandomNumberProviderBuilder<T> providerBuilder = (AbstractRandomNumberProviderBuilder<T>) maxResult;
    final T random = providerBuilder.build().get();
    assertEquals("Returned random number be equal to upper bound.", someValidNumberWithinRange, random);
  }

  @SuppressWarnings("MagicNumber")
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    final RandomProvider mock = mock(RandomProvider.class);
    return defaultParametersBuilder(RandomNumberProviderImplsTest.class)
            .add(mock, new RandomIntegerProvider(mock), 42)
            .add(mock, new RandomLongProvider(mock), 42L)
            .build();
  }
}

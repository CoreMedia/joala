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

package net.joala.data.random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Tests {@link RandomNumberProvider} and its implementations.
 * </p>
 *
 * @since 9/14/12
 */
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
    return Arrays.asList(new Object[][]{
            {new RandomDoubleProvider()},
            {new RandomFloatProvider()},
            {new RandomIntegerProvider()},
            {new RandomLongProvider()},
    });
  }
}

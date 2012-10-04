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

import net.joala.lab.junit.template.TestToString;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.Random;

import static java.lang.Math.random;
import static net.joala.lab.junit.template.TestToString.testToString;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isNull;

/**
 * <p>
 * Tests {@link AbstractRandomNumberType}.
 * </p>
 *
 * @since 9/17/12
 */
public class AbstractRandomNumberTypeTest {
  private final Class<Integer> type = Integer.class;
  private static final int SOME_INTEGER = new Random().nextInt();

  @Test
  public void constructor_should_accept_nonnull_type() throws Exception {
    try {
      new SimpleRandomNumberType(type);
    } catch (Throwable ignored) {
      fail("Should have created number type successfully.");
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Test(expected = NullPointerException.class)
  public void constructor_should_deny_type_being_null() throws Exception {
    new SimpleRandomNumberType(null);
  }

  @Test
  public void getType_should_return_type_as_specified_in_constructor() throws Exception {
    assertSame(type, new SimpleRandomNumberType(type).getType());
  }

  @Test
  public void toString_should_contain_type_description() throws Exception {
    assertThat(new SimpleRandomNumberType(type).toString(), containsString(String.valueOf(type)));
  }

  @Test
  public void toString_should_contain_necessary_information() throws Throwable {
    testToString(new SimpleRandomNumberType(type));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void lower_bound_percentage_should_be_accepted() throws Exception {
    try {
      new SimpleRandomNumberType(type).percentOf(0d, SOME_INTEGER);
    } catch (Throwable e) {
      assertThat("No exception should have been thrown.", e, (Matcher<Throwable>) isNull());
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void upper_bound_percentage_should_be_accepted() throws Exception {
    try {
      new SimpleRandomNumberType(type).percentOf(1d, SOME_INTEGER);
    } catch (Throwable e) {
      assertThat("No exception should have been thrown.", e, (Matcher<Throwable>) isNull());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void lower_outofbounds_percentage_should_be_denied() throws Exception {
    new SimpleRandomNumberType(type).percentOf(-1d, SOME_INTEGER);
  }

  @Test(expected = IllegalArgumentException.class)
  public void upper_outofbounds_percentage_should_be_denied() throws Exception {
    new SimpleRandomNumberType(type).percentOf(Double.MAX_VALUE, SOME_INTEGER);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void inbetween_percentage_should_be_accepted() throws Exception {
    final double value = random();
    try {
      new SimpleRandomNumberType(type).percentOf(value, SOME_INTEGER);
    } catch (Throwable e) {
      assertThat(String.format("No exception should have been thrown for value %f.", value), e, (Matcher<Throwable>) isNull());
    }
  }

  private static final class SimpleRandomNumberType extends AbstractRandomNumberType<Integer> {

    /**
     * <p>
     * Constructor specifying type of random numbers.
     * </p>
     *
     * @param type type of numbers
     */
    private SimpleRandomNumberType(@Nonnull final Class<Integer> type) {
      super(type);
    }

    @Nonnull
    @Override
    public Integer min() {
      return SOME_INTEGER;
    }

    @Nonnull
    @Override
    public Integer max() {
      return SOME_INTEGER;
    }

    @Nonnull
    @Override
    public Integer sum(final Integer value1, final Integer value2) {
      return SOME_INTEGER;
    }

    @Nonnull
    @Override
    public Integer percentOf(final double percent, final Integer value) {
      checkPercentageArgument(percent);
      return SOME_INTEGER;
    }
  }
}

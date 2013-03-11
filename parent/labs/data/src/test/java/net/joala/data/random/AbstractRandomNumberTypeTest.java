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

import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.Random;

import static java.lang.Math.random;
import static net.joala.testlet.ToStringTestlet.toStringTestlet;
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
    toStringTestlet(new SimpleRandomNumberType(type)).run();
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

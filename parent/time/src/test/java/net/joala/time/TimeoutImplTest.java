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

package net.joala.time;

import net.joala.internal.data.DataProvider;
import net.joala.data.random.RandomDoubleProvider;
import net.joala.data.random.RandomIntegerProvider;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.round;
import static java.lang.String.format;
import static net.joala.testlet.ToStringTestlet.toStringTestlet;
import static org.junit.Assert.assertEquals;

/**
 * Tests {@link TimeoutImpl}.
 *
 * @since 8/25/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
public class TimeoutImplTest {
  private final DataProvider<Integer> randomPositiveInt = new RandomIntegerProvider().min(0).max(Integer.MAX_VALUE).fixate();
  private final DataProvider<Double> randomPositiveDouble = new RandomDoubleProvider().min(0d).max(Double.MAX_VALUE).fixate();

  @Test(expected = IllegalArgumentException.class)
  public void constructor_should_throw_exception_on_negative_timeout() throws Exception {
    new TimeoutImpl(-1L, TimeUnit.MILLISECONDS);
  }

  @SuppressWarnings("ConstantConditions")
  @Test(expected = NullPointerException.class)
  public void constructor_should_throw_exception_on_invalid_timeunit() throws Exception {
    new TimeoutImpl(randomPositiveInt.get(), null);
  }

  @Test
  public void constructor_should_set_amount_and_unit_correctly() throws Exception {
    final int amount = randomPositiveInt.get();
    final TimeUnit unit = TimeUnit.SECONDS;
    final Timeout timeout = new TimeoutImpl(amount, unit);
    assertEquals("Constructor should have correctly set amount and unit.", unit.toMillis(amount), timeout.in(TimeUnit.MILLISECONDS));
  }

  @Test
  public void in_method_should_convert_correctly() throws Exception {
    final int amount = randomPositiveInt.get();
    final TimeUnit unit = TimeUnit.SECONDS;
    final Timeout timeout = new TimeoutImpl(amount, unit);
    assertEquals(format("Correctly converted to Milliseconds: %s", timeout), unit.toMillis(amount), timeout.in(TimeUnit.MILLISECONDS));
    assertEquals(format("Correctly converted to Seconds: %s", timeout), unit.toSeconds(amount), timeout.in(TimeUnit.SECONDS));
    assertEquals(format("Correctly converted to Minutes: %s", timeout), unit.toMinutes(amount), timeout.in(TimeUnit.MINUTES));
  }

  @SuppressWarnings("ConstantConditions")
  @Test(expected = NullPointerException.class)
  public void in_method_should_throw_exception_on_null_timeunit() throws Exception {
    final int amount = randomPositiveInt.get();
    final TimeUnit unit = TimeUnit.SECONDS;
    final Timeout timeout = new TimeoutImpl(amount, unit);
    timeout.in(null);
  }

  @Test
  public void in_method_should_correctly_apply_positive_factor() throws Exception {
    final double factor = randomPositiveDouble.get();
    final int amount = randomPositiveInt.get();
    final TimeUnit unit = TimeUnit.SECONDS;
    final Timeout timeout = new TimeoutImpl(amount, unit);
    assertEquals(format("Correctly converted to Milliseconds: %s", timeout), round(unit.toMillis(amount) * factor), timeout.in(TimeUnit.MILLISECONDS, factor));
    assertEquals(format("Correctly converted to Seconds: %s", timeout), round(unit.toSeconds(amount) * factor), timeout.in(TimeUnit.SECONDS, factor));
  }

  @Test(expected = IllegalArgumentException.class)
  public void in_method_should_throw_exception_on_non_positive_factor() throws Exception {
    final double factor = -1.0 * randomPositiveDouble.get();
    final int amount = randomPositiveInt.get();
    final TimeUnit unit = TimeUnit.SECONDS;
    final Timeout timeout = new TimeoutImpl(amount, unit);
    timeout.in(unit, factor);
  }

  @Test
  public void toString_should_contain_necessary_information() throws Throwable {
    final int amount = randomPositiveInt.get();
    final TimeUnit unit = TimeUnit.SECONDS;
    final Timeout timeout = new TimeoutImpl(amount, unit);
    toStringTestlet(timeout).run();
  }

}

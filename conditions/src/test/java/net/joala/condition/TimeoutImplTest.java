package net.joala.condition;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.round;
import static java.lang.String.format;
import static net.joala.condition.RandomData.randomPositiveDouble;
import static net.joala.condition.RandomData.randomPositiveInt;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link TimeoutImpl}.
 *
 * @since 8/25/12
 */
public class TimeoutImplTest {

  @Test(expected = IllegalArgumentException.class)
  public void constructor_should_throw_exception_on_negative_timeout() throws Exception {
    new TimeoutImpl(-1l, TimeUnit.MILLISECONDS);
  }

  @SuppressWarnings("ConstantConditions")
  @Test(expected = NullPointerException.class)
  public void constructor_should_throw_exception_on_invalid_timeunit() throws Exception {
    new TimeoutImpl(randomPositiveInt(), null);
  }

  @Test
  public void constructor_should_set_amount_and_unit_correctly() throws Exception {
    final int amount = randomPositiveInt();
    final TimeUnit unit = TimeUnit.SECONDS;
    final Timeout timeout = new TimeoutImpl(amount, unit);
    assertEquals("Constructor should have correctly set amount and unit.", unit.toMillis(amount), timeout.in(TimeUnit.MILLISECONDS));
  }

  @Test
  public void in_method_should_convert_correctly() throws Exception {
    final int amount = randomPositiveInt();
    final TimeUnit unit = TimeUnit.SECONDS;
    final Timeout timeout = new TimeoutImpl(amount, unit);
    assertEquals(format("Correctly converted to Milliseconds: %s", timeout), unit.toMillis(amount), timeout.in(TimeUnit.MILLISECONDS));
    assertEquals(format("Correctly converted to Seconds: %s", timeout), unit.toSeconds(amount), timeout.in(TimeUnit.SECONDS));
    assertEquals(format("Correctly converted to Minutes: %s", timeout), unit.toMinutes(amount), timeout.in(TimeUnit.MINUTES));
  }

  @SuppressWarnings("ConstantConditions")
  @Test(expected = NullPointerException.class)
  public void in_method_should_throw_exception_on_null_timeunit() throws Exception {
    final int amount = randomPositiveInt();
    final TimeUnit unit = TimeUnit.SECONDS;
    final Timeout timeout = new TimeoutImpl(amount, unit);
    timeout.in(null);
  }

  @Test
  public void in_method_should_correctly_apply_positive_factor() throws Exception {
    final double factor = randomPositiveDouble();
    final int amount = randomPositiveInt();
    final TimeUnit unit = TimeUnit.SECONDS;
    final Timeout timeout = new TimeoutImpl(amount, unit);
    assertEquals(format("Correctly converted to Milliseconds: %s", timeout), round(unit.toMillis(amount) * factor), timeout.in(TimeUnit.MILLISECONDS, factor));
    assertEquals(format("Correctly converted to Seconds: %s", timeout), round(unit.toSeconds(amount) * factor), timeout.in(TimeUnit.SECONDS, factor));
  }

  @Test(expected = IllegalArgumentException.class)
  public void in_method_should_throw_exception_on_non_positive_factor() throws Exception {
    final double factor = -1.0 * randomPositiveDouble();
    final int amount = randomPositiveInt();
    final TimeUnit unit = TimeUnit.SECONDS;
    final Timeout timeout = new TimeoutImpl(amount, unit);
    timeout.in(unit, factor);
  }

  @Test
  public void toString_should_contain_parameters() throws Exception {
    final int amount = randomPositiveInt();
    final TimeUnit unit = TimeUnit.SECONDS;
    final Timeout timeout = new TimeoutImpl(amount, unit);
    final String str = timeout.toString();
    assertThat(str, containsString(String.valueOf(amount)));
    assertThat(str, containsString(String.valueOf(unit)));
  }
}

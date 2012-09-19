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

package net.joala.condition.timing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static net.joala.matcher.text.MatchesPattern.matchesPattern;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Tests {@link TimeFormat}.
 * </p>
 *
 * @since 9/19/12
 */
@RunWith(Parameterized.class)
public class TimeFormatTest {
  private final String message;
  private final long amount;
  private final TimeUnit timeUnit;
  private final Pattern exepectedTimePattern;

  public TimeFormatTest(final long amount, final TimeUnit timeUnit, final String exepectedTimePattern) {
    this.timeUnit = timeUnit;
    this.exepectedTimePattern = Pattern.compile(exepectedTimePattern);
    this.message = String.format("Format amount %d of %s.", amount, timeUnit);
    this.amount = amount;
  }

  @Test
  public void time_should_be_formatted_as_expected() throws Exception {
    final String description = TimeFormat.format(amount, timeUnit);
    assertThat(message, description, matchesPattern(exepectedTimePattern));
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
            {TimeUnit.SECONDS.toMillis(TimeFormat.TIMEUNIT_LIMIT) - 1, TimeUnit.MILLISECONDS, "^1999\\sms$"},
            {TimeUnit.SECONDS.toMillis(TimeFormat.TIMEUNIT_LIMIT), TimeUnit.MILLISECONDS, "^2\\ss$"},
            {TimeUnit.MINUTES.toMillis(TimeFormat.TIMEUNIT_LIMIT) - 1, TimeUnit.MILLISECONDS, "^119\\ss$"},
            {TimeUnit.MINUTES.toMillis(TimeFormat.TIMEUNIT_LIMIT), TimeUnit.MILLISECONDS, "^2\\smin$"},
            {TimeUnit.HOURS.toMillis(TimeFormat.TIMEUNIT_LIMIT) - 1, TimeUnit.MILLISECONDS, "^119\\smin$"},
            {TimeUnit.HOURS.toMillis(TimeFormat.TIMEUNIT_LIMIT), TimeUnit.MILLISECONDS, "^2\\sh$"},
            {TimeUnit.DAYS.toMillis(TimeFormat.TIMEUNIT_LIMIT) - 1, TimeUnit.MILLISECONDS, "^47\\sh$"},
            {TimeUnit.DAYS.toMillis(TimeFormat.TIMEUNIT_LIMIT), TimeUnit.MILLISECONDS, "^2\\sd$"},
            {Long.MAX_VALUE, TimeUnit.MILLISECONDS, "^\\d+\\sd$"},
            {0l, TimeUnit.NANOSECONDS, "^0\\sns$"},
            {0l, TimeUnit.MICROSECONDS, "^0\\sµs$"},
            {0l, TimeUnit.MILLISECONDS, "^0\\sms$"},
            {0l, TimeUnit.SECONDS, "^0\\ss$"},
            {0l, TimeUnit.MINUTES, "^0\\smin$"},
            {0l, TimeUnit.HOURS, "^0\\sh$"},
            {0l, TimeUnit.DAYS, "^0\\sd$"},
    });
  }

}

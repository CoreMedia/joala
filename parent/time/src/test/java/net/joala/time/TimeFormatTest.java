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

package net.joala.time;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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
  private final Pattern expectedTimePattern;

  public TimeFormatTest(final long amount, final TimeUnit timeUnit, final String expectedTimePattern) {
    this.timeUnit = timeUnit;
    this.expectedTimePattern = Pattern.compile(expectedTimePattern);
    this.message = String.format("Format amount %d of %s.", amount, timeUnit);
    this.amount = amount;
  }

  @Test
  public void time_should_be_formatted_as_expected() throws Exception {
    final String description = TimeFormat.format(amount, timeUnit);
    assertThat(message, description, new MatchesPattern(expectedTimePattern));
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
            {0L, TimeUnit.NANOSECONDS, "^0\\sns$"},
            {0L, TimeUnit.MICROSECONDS, "^0\\sµs$"},
            {0L, TimeUnit.MILLISECONDS, "^0\\sms$"},
            {0L, TimeUnit.SECONDS, "^0\\ss$"},
            {0L, TimeUnit.MINUTES, "^0\\smin$"},
            {0L, TimeUnit.HOURS, "^0\\sh$"},
            {0L, TimeUnit.DAYS, "^0\\sd$"},
    });
  }

  // Duplicate in order to remove dependency cycles between modules.
  private static final class MatchesPattern extends TypeSafeMatcher<CharSequence> {
    private final Pattern pattern;

    private MatchesPattern(final Pattern pattern) {
      this.pattern = pattern;
    }

    @Override
    public void describeTo(final Description description) {
      description.appendText("a sequence of characters matching pattern ");
      description.appendValue(pattern);
    }

    @Override
    protected boolean matchesSafely(final CharSequence item) {
      return pattern.matcher(item).matches();
    }
  }
}

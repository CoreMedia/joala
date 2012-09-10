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

package net.joala.condition;

import org.hamcrest.SelfDescribing;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static net.joala.condition.RandomData.randomString;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Tests {@link AbstractConditionWaitFailStrategy}.
 * </p>
 *
 * @since 9/4/12
 */
@RunWith(Parameterized.class)
public class AbstractConditionWaitFailStrategyTimeoutDescriptionMillisTest {
  private final String message;
  private final long millis;
  private final String exepectedTime;

  public AbstractConditionWaitFailStrategyTimeoutDescriptionMillisTest(final String message, final long millis, final String exepectedTime) {
    this.exepectedTime = exepectedTime;
    this.message = message;
    this.millis = millis;
  }

  @Test
  public void time_should_be_formatted_as_expected() throws Exception {
    final AbstractConditionWaitFailStrategy strategy = mock(AbstractConditionWaitFailStrategy.class);
    when(strategy.addTimeoutDescription(anyString(), any(SelfDescribing.class), anyLong())).thenCallRealMethod();
    final String description = strategy.addTimeoutDescription(randomString("message"), mock(SelfDescribing.class), millis);
    assertThat(message, description, containsString(exepectedTime));
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
            {"Lower bound to millis", 0l, "0 ms"},
            {"Upper bound to millis", TimeUnit.SECONDS.toMillis(2) - 1, "1999 ms"},
            {"Lower bound to seconds", TimeUnit.SECONDS.toMillis(2), "2 s"},
            {"Upper bound to seconds", TimeUnit.MINUTES.toMillis(2) - 1, "119 s"},
            {"Lower bound to minutes", TimeUnit.MINUTES.toMillis(2), "2 min"},
            {"Upper bound to minutes", TimeUnit.HOURS.toMillis(2) - 1, "119 min"},
            {"Lower bound to hours", TimeUnit.HOURS.toMillis(2), "2 h"},
            {"Upper bound to hours; just checking that 'h' is contained in response", Long.MAX_VALUE, " h "},
    });
  }
}

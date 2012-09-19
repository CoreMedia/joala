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

import net.joala.data.DataProvider;
import net.joala.data.DataProvidingException;
import net.joala.data.random.DefaultRandomStringProvider;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests {@link WaitTimeoutException}.
 *
 * @since 8/24/12
 */
@RunWith(Parameterized.class)
public class WaitTimeoutExceptionTest {
  private static final DataProvider<String> STRING_PROVIDER = new DefaultRandomStringProvider().fixate();

  @Test(expected = WaitTimeoutException.class)
  public void should_be_able_to_call_constructor_without_exception() throws Exception {
    throw new WaitTimeoutException(exceptionMessage, exceptionCause);
  }

  @Test
  public void message_should_be_set_correctly() throws Exception {
    try {
      throw new WaitTimeoutException(exceptionMessage, exceptionCause);
    } catch (WaitTimeoutException e) {
      assertEquals(testMessage, exceptionMessage, e.getMessage());
    }
  }

  @Test
  public void cause_should_be_set_correctly() throws Exception {
    try {
      throw new WaitTimeoutException(exceptionMessage, exceptionCause);
    } catch (WaitTimeoutException e) {
      assertSame(testMessage, exceptionCause, e.getCause());
    }
  }

  @Parameterized.Parameters
  public static Collection<Object[]> parameters() throws DataProvidingException {
    Math.random();
    return Arrays.asList(new Object[][]{
            {"Should except both, message and cause to be null.", null, null},
            {"Should except both, message and cause not to be null.", STRING_PROVIDER.get(), new Exception(STRING_PROVIDER.get())},
            {"Should except message to be null, while cause is non-null.", null, new Exception(STRING_PROVIDER.get())},
            {"Should except cause to be null, while message is non-null.", STRING_PROVIDER.get(), null},
    });
  }

  private final String testMessage;
  private final String exceptionMessage;
  private final Throwable exceptionCause;

  public WaitTimeoutExceptionTest(final String testMessage, final String exceptionMessage, final Throwable exceptionCause) {
    this.exceptionCause = exceptionCause;
    this.testMessage = testMessage;
    this.exceptionMessage = exceptionMessage;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
            append("testMessage", testMessage).
            append("exceptionMessage", exceptionMessage).
            append("exceptionCause", exceptionCause).
            toString();
  }

}

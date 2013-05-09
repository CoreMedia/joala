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

package net.joala.condition.timing;

import net.joala.internal.data.DataProvider;
import net.joala.internal.data.DataProvidingException;
import net.joala.data.random.DefaultRandomStringProvider;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static net.joala.internal.junit.ParameterizedParametersBuilders.defaultParametersBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests {@link WaitTimeoutException}.
 *
 * @since 8/24/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
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
    return defaultParametersBuilder(WaitTimeoutExceptionTest.class)
            .add("Should except both, message and cause to be null.", null, null)
            .add("Should except both, message and cause not to be null.", STRING_PROVIDER.get(), new Exception(STRING_PROVIDER.get()))
            .add("Should except message to be null, while cause is non-null.", null, new Exception(STRING_PROVIDER.get()))
            .add("Should except cause to be null, while message is non-null.", STRING_PROVIDER.get(), null)
            .build();
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

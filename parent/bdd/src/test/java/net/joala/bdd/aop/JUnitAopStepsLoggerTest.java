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

package net.joala.bdd.aop;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collection;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @since 6/1/12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JUnitAopStepsLoggerTest {
  @Inject
  private Steps _;

  @Before
  public void setUp() throws Exception {
    JUnitAopStepsLoggerTestAppender.clearEvents();
  }

  @After
  public void tearDown() throws Exception {
    JUnitAopStepsLoggerTestAppender.clearEvents();
  }

  public void assertMessagesContainsStepDescription(final String expectedString) {
    final Collection<ILoggingEvent> events = JUnitAopStepsLoggerTestAppender.getEvents();
    boolean passedAtLeastOnce = false;
    AssertionError lastError = null;
    for (final ILoggingEvent event : events) {
      try {
        assertThat(event.getFormattedMessage(), Matchers.containsString(expectedString));
        passedAtLeastOnce = true;
      } catch (AssertionError e) {
        lastError = e;
      }
    }
    if (!passedAtLeastOnce) {
      throw lastError;
    }
  }

  @Test
  public void testGivenPointcut() throws Exception {
    Assume.assumeThat(JUnitAopStepsLoggerTestAppender.getEvents().size(), Matchers.equalTo(0));
    _.given_this_is_a_test___();
    assertMessagesContainsStepDescription("given this is a test");
  }

  @Test
  public void testWhenPointcut() throws Exception {
    Assume.assumeThat(JUnitAopStepsLoggerTestAppender.getEvents().size(), Matchers.equalTo(0));
    _.when_this_is_a_test___();
    assertMessagesContainsStepDescription("when this is a test");
  }

  @Test
  public void testFailingWhenPointcut() throws Exception {
    Assume.assumeThat(JUnitAopStepsLoggerTestAppender.getEvents().size(), Matchers.equalTo(0));
    try {
      _.when_assumption_fails___();
    } catch (AssumptionViolatedException ignored) {
    }
    assertMessagesContainsStepDescription("when assumption fails");
    assertMessagesContainsStepDescription("FAILED");
  }

  @Test
  public void testThenPointcut() throws Exception {
    Assume.assumeThat(JUnitAopStepsLoggerTestAppender.getEvents().size(), Matchers.equalTo(0));
    _.then_this_is_a_test___();
    assertMessagesContainsStepDescription("then this is a test");
  }

  @Test
  public void testFailingThenPointcut() throws Exception {
    Assume.assumeThat(JUnitAopStepsLoggerTestAppender.getEvents().size(), Matchers.equalTo(0));
    try {
      _.then_I_fail___();
    } catch (AssertionError ignored) {
    }
    assertMessagesContainsStepDescription("then I fail");
    assertMessagesContainsStepDescription("FAILED");
  }

  @Named
  @Singleton
  public static class Steps {
    public void given_this_is_a_test___() {
    }

    public void when_this_is_a_test___() {
    }

    public void when_assumption_fails___() {
      Assume.assumeTrue(false);
    }

    public void then_this_is_a_test___() {
    }

    public void then_I_fail___() {
      fail("I will fail.");
    }

  }

}

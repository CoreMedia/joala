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

package net.joala.matcher.decorator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static java.lang.String.format;
import static net.joala.matcher.decorator.EnhanceDescriptionBy.enhanceDescriptionBy;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

/**
 * <p>
 * Tests {@link EnhanceDescriptionBy}.
 * </p>
 *
 * @since 9/5/12
 */
@RunWith(MockitoJUnitRunner.class)
public class EnhanceDescriptionByTest {
  private static final int RANDOM_STRING_LENGTH = 16;
  @Mock
  private Matcher<String> matcher;
  private final Description description = new StringDescription();
  private final String plainDescriptionTemplate = format("%s_tpl", random(RANDOM_STRING_LENGTH));
  private final String arg1DescriptionTemplate = format("%s_%%0_tpl", random(RANDOM_STRING_LENGTH));
  private final String matcherDescription = format("%s_matcher", random(RANDOM_STRING_LENGTH));

  @Before
  public void setUp() throws Exception {
    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(final InvocationOnMock invocation) throws Throwable {
        final Object[] arguments = invocation.getArguments();
        assumeThat(arguments, arrayWithSize(1));
        assumeThat(arguments[0], instanceOf(Description.class));
        final Description desc = (Description) arguments[0];
        desc.appendText(matcherDescription);
        return null;
      }
    }).when(matcher).describeTo(any(Description.class));
  }

  @Test
  public void describeTo_should_add_matcher_description() throws Exception {
    final SelfDescribing descMatcher = new EnhanceDescriptionBy<String>(plainDescriptionTemplate, matcher);
    descMatcher.describeTo(description);
    assertThat("Matcher description should be part of the description.", description.toString(), Matchers.containsString(matcherDescription));
  }

  @Test
  public void describeTo_should_add_enhanced_description() throws Exception {
    final SelfDescribing descMatcher = new EnhanceDescriptionBy<String>(plainDescriptionTemplate, matcher);
    descMatcher.describeTo(description);
    assertThat("Enhanced description should be part of the description.", description.toString(), Matchers.containsString(plainDescriptionTemplate));
  }

  @Test
  public void describeTo_should_add_template_arguments() throws Exception {
    final String argument = format("%sarg0", random(RANDOM_STRING_LENGTH));
    final SelfDescribing descMatcher = new EnhanceDescriptionBy<String>(arg1DescriptionTemplate, matcher, argument);
    descMatcher.describeTo(description);
    assertThat("Argument should be contained in description.", description.toString(), Matchers.containsString(argument));
  }

  @Test
  public void describeTo_should_add_matcher_description_using_factory() throws Exception {
    final Matcher<String> descMatcher = enhanceDescriptionBy(plainDescriptionTemplate, matcher);
    descMatcher.describeTo(description);
    assertThat("Matcher description should be part of the description.", description.toString(), Matchers.containsString(matcherDescription));
  }

  @Test
  public void describeTo_should_add_enhanced_description_using_factory() throws Exception {
    final Matcher<String> descMatcher = enhanceDescriptionBy(plainDescriptionTemplate, matcher);
    descMatcher.describeTo(description);
    assertThat("Enhanced description should be part of the description.", description.toString(), Matchers.containsString(plainDescriptionTemplate));
  }

  @Test
  public void describeTo_should_add_template_arguments_using_factory() throws Exception {
    final String argument = format("%sarg0", random(RANDOM_STRING_LENGTH));
    final Matcher<String> descMatcher = enhanceDescriptionBy(arg1DescriptionTemplate, matcher, argument);
    descMatcher.describeTo(description);
    assertThat("Argument should be contained in description.", description.toString(), Matchers.containsString(argument));
  }

}

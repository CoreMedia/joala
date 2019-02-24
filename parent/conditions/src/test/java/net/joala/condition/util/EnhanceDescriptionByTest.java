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

package net.joala.condition.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static java.lang.String.format;
import static net.joala.condition.util.EnhanceDescriptionBy.enhanceDescriptionBy;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.mockito.ArgumentMatchers.any;
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
  public void setUp() {
    doAnswer((Answer<Void>) invocation -> {
      final Object[] arguments = invocation.getArguments();
      assumeThat(arguments, arrayWithSize(1));
      assumeThat(arguments[0], instanceOf(Description.class));
      final Description desc = (Description) arguments[0];
      desc.appendText(matcherDescription);
      return null;
    }).when(matcher).describeTo(any(Description.class));
  }

  @Test
  public void describeTo_should_add_matcher_description() {
    final SelfDescribing descMatcher = new EnhanceDescriptionBy<>(plainDescriptionTemplate, matcher);
    descMatcher.describeTo(description);
    assertThat("Matcher description should be part of the description.", description.toString(), Matchers.containsString(matcherDescription));
  }

  @Test
  public void describeTo_should_add_enhanced_description() {
    final SelfDescribing descMatcher = new EnhanceDescriptionBy<>(plainDescriptionTemplate, matcher);
    descMatcher.describeTo(description);
    assertThat("Enhanced description should be part of the description.", description.toString(), Matchers.containsString(plainDescriptionTemplate));
  }

  @Test
  public void describeTo_should_add_template_arguments() {
    final String argument = format("%sarg0", random(RANDOM_STRING_LENGTH));
    final SelfDescribing descMatcher = new EnhanceDescriptionBy<>(arg1DescriptionTemplate, matcher, argument);
    descMatcher.describeTo(description);
    assertThat("Argument should be contained in description.", description.toString(), Matchers.containsString(argument));
  }

  @Test
  public void describeTo_should_add_matcher_description_using_factory() {
    final Matcher<String> descMatcher = enhanceDescriptionBy(plainDescriptionTemplate, matcher);
    descMatcher.describeTo(description);
    assertThat("Matcher description should be part of the description.", description.toString(), Matchers.containsString(matcherDescription));
  }

  @Test
  public void describeTo_should_add_enhanced_description_using_factory() {
    final Matcher<String> descMatcher = enhanceDescriptionBy(plainDescriptionTemplate, matcher);
    descMatcher.describeTo(description);
    assertThat("Enhanced description should be part of the description.", description.toString(), Matchers.containsString(plainDescriptionTemplate));
  }

  @Test
  public void describeTo_should_add_template_arguments_using_factory() {
    final String argument = format("%sarg0", random(RANDOM_STRING_LENGTH));
    final Matcher<String> descMatcher = enhanceDescriptionBy(arg1DescriptionTemplate, matcher, argument);
    descMatcher.describeTo(description);
    assertThat("Argument should be contained in description.", description.toString(), Matchers.containsString(argument));
  }

}

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
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.core.DescribedAs;

/**
 * <p>
 * Use this wrapper for matchers to enhance it by additional
 * description. Can be used for example for assumption failures
 * where with current API it is not possible to add a message.
 * </p>
 *
 * @see <a href="https://github.com/KentBeck/junit/pull/489">Improve Assume to allow custom message</a>
 * @since 8/28/12
 */
public class EnhanceDescriptionBy<T> extends DescribedAs<T> {

  private final Matcher<T> matcher;

  public EnhanceDescriptionBy(final String descriptionTemplate, final Matcher<T> matcher, final Object... values) {
    super(descriptionTemplate, matcher, values);
    this.matcher = matcher;
  }

  @Override
  public void describeTo(final Description description) {
    matcher.describeTo(description);
    description.appendText(" (");
    super.describeTo(description);
    description.appendText(")");
  }

  @Factory
  public static <T> Matcher<T> enhanceDescriptionBy(final String descriptionTemplate, final Matcher<T> matcher, final Object... values) {
    return new EnhanceDescriptionBy<T>(descriptionTemplate, matcher, values);
  }

}

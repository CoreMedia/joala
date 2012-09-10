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

package net.joala.matcher.exception;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import static java.lang.String.format;
import static org.hamcrest.core.StringContains.containsString;

/**
 * @since 8/28/12
 */
public class MessageContains extends CustomTypeSafeMatcher<Throwable> {
  private final String containedMessage;
  private final boolean recurseCauses;

  public MessageContains(final String containedMessage) {
    this(containedMessage, false);
  }

  public MessageContains(final String containedMessage, final boolean recurseCauses) {
    super(format("message contains '%s'", containedMessage));
    this.containedMessage = containedMessage;
    this.recurseCauses = recurseCauses;
  }

  @Override
  protected boolean matchesSafely(final Throwable item) {
    return containsString(containedMessage).matches(item.getMessage())
            || (recurseCauses && item.getCause() != null && matchesSafely(item.getCause()));
  }

  @Factory
  public static Matcher<Throwable> messageContains(final String expectedMessage, final boolean recurseCauses) {
    return new MessageContains(expectedMessage, recurseCauses);
  }

  @Factory
  public static Matcher<Throwable> messageContains(final String expectedMessage) {
    return new MessageContains(expectedMessage);
  }

}

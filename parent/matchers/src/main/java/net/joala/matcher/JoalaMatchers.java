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

package net.joala.matcher;

import net.joala.matcher.decorator.EnhanceDescriptionBy;
import net.joala.matcher.exception.CausedBy;
import net.joala.matcher.exception.MessageContains;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;

/**
 * @since 8/28/12
 */
public final class JoalaMatchers {
  private JoalaMatchers() {
  }

  public static Matcher<Throwable> causedBy(final Throwable expected) {
    return CausedBy.causedBy(expected);
  }

  public static Matcher<Throwable> messageContains(final String expected) {
    return MessageContains.messageContains(expected);
  }

  public static Matcher<Throwable> messageContains(final String expected, final boolean recurseCauses) {
    return MessageContains.messageContains(expected, recurseCauses);
  }

  public static <T> Matcher<T> enhanceDescriptionBy(@Nonnull final String descriptionTemplate,
                                                    @Nonnull final Matcher<T> matcher,
                                                    final Object... values) {
    return EnhanceDescriptionBy.enhanceDescriptionBy(descriptionTemplate, matcher, values);
  }
}

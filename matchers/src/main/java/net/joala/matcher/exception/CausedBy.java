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

import javax.annotation.Nonnull;

import static org.hamcrest.core.IsEqual.equalTo;

/**
 * <p>
 * Matcher to search stacktrace of exception if it get caused by a given exception somewhere.
 * </p>
 *
 * @since 8/28/12
 */
public class CausedBy extends CustomTypeSafeMatcher<Throwable> {
  private final Throwable cause;

  public CausedBy(@Nonnull final Throwable cause) {
    super("is caused by");
    this.cause = cause;
  }

  @Override
  protected boolean matchesSafely(final Throwable item) {
    return item.getCause() != null && (item.getCause() == cause || matchesSafely(item.getCause()));
  }

  @Factory
  public static Matcher<Throwable> causedBy(final Throwable expected) {
    return new CausedBy(expected);
  }

}

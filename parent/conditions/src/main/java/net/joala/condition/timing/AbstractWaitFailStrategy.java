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

import org.hamcrest.Description;
import org.hamcrest.StringDescription;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

import static net.joala.matcher.DescriptionUtil.describeTo;

/**
 * <p>
 * Abstract implementation of {@link WaitFailStrategy}. Provides some commonly used
 * methods for fail strategy implementations.
 * </p>
 *
 * @since 8/27/12
 */
public abstract class AbstractWaitFailStrategy implements WaitFailStrategy {

  /**
   * <p>
   * Enhances the description for a failure message by the evaluated function and how long it took to
   * meet the timeout.
   * </p>
   *
   * @param message        original (plain) message
   * @param function       function evaluated
   * @param input          the input to the function
   * @param consumedMillis how long it took until timeout  @return enhanced message
   */
  @Nonnull
  protected String addTimeoutDescription(@Nullable final String message,
                                         @Nonnull final Object function,
                                         @Nonnull final Object input,
                                         @Nonnegative final long consumedMillis) {
    final Description description = new StringDescription();
    description.appendText(message == null ? "Failed to evaluate." : message);
    description.appendText(" - after ");
    description.appendText(TimeFormat.format(consumedMillis, TimeUnit.MILLISECONDS));
    description.appendText(" evaluating ");
    describeTo(description, function);
    description.appendText(" on ");
    describeTo(description, input);
    return description.toString();
  }

}

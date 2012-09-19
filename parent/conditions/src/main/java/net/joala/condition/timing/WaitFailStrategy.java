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

import org.hamcrest.Matcher;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p>
 * Strategy what to do if a condition does not get fulfilled within time. Possible options
 * are to signal just the timeout or that an assertion/assumption failed.
 * </p>
 *
 * @since 8/23/12
 */
public interface WaitFailStrategy {
  /**
   * Makes a condition fail because the expected value did not get returned in time.
   *
   * @param reason         reason of the failure
   * @param function       function evaluated
   * @param input          original input to the function
   * @param lastValue      the last value retrieved via function
   * @param matcher        the matcher which did not match
   * @param consumedMillis consumed milliseconds
   * @param <T>            the value type returned by the function
   */
  <T> void fail(@Nullable String reason,
                @Nonnull Object function,
                @Nonnull Object input,
                @Nullable T lastValue,
                @Nonnull Matcher<? super T> matcher,
                @Nonnegative long consumedMillis);

  /**
   * Makes a condition fail because the expected value could not be retrieved because of repeating
   * evaluation exceptions.
   *
   * @param reason         reason of the failure
   * @param function       function evaluated
   * @param input          original input to the function
   * @param throwable      last exception which got caught
   * @param consumedMillis consumed milliseconds
   */
  void fail(@Nullable String reason,
            @Nonnull Object function,
            @Nonnull Object input,
            @Nonnull Throwable throwable,
            @Nonnegative long consumedMillis);
}

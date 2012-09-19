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

import com.google.common.base.Function;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p>
 * Wait until an expectation is met. Validate the expectation with decelerating polling intervals.
 * </p>
 *
 * @since 9/14/12
 */
public interface Wait {
  /**
   * <p>
   * Wait until the given input passes its state query without ignorable exception.
   * </p>
   * <p>
   * This call is equal to:
   * </p>
   * <pre>{@code
   * until(null, input, stateQuery, null)
   * }</pre>
   *
   * @param input      the input to pass to the query function; if implementing {@link SelfDescribing} the
   *                   description of input will be queried on failure
   * @param stateQuery the function to query the state of input; if implementing {@link SelfDescribing} the
   *                   description of stateQuery will be queried on failure
   * @param <F>        the input type
   * @param <T>        the return type of the state query
   * @return the result of the successful state query
   * @see IgnorableStateQueryException
   */
  <F, T> T until(@Nonnull F input,
                 @Nonnull Function<? super F, T> stateQuery);

  /**
   * <p>
   * Wait until an expectation is met. Validate the expectation with decelerating polling intervals.
   * </p>
   *
   * @param message    the message to print on failure; {@code null} for no additional message
   * @param input      the input to pass to the query function; if implementing {@link SelfDescribing} the
   *                   description of input will be queried on failure
   * @param stateQuery the function to query the state of input; if implementing {@link SelfDescribing} the
   *                   description of stateQuery will be queried on failure
   * @param matcher    the matcher to validate the result of the query; {@code null} to match any returned value
   * @param <F>        the input type
   * @param <T>        the return type of the state query
   */
  <F, T> T until(@Nullable String message,
                 @Nonnull F input,
                 @Nonnull Function<? super F, T> stateQuery,
                 @Nullable Matcher<? super T> matcher);
}

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

package net.joala.base;

import com.google.common.base.Function;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import javax.annotation.Nonnull;

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
   * Wait until an expectation is met. Validate the expectation with decelerating polling intervals.
   * </p>
   *
   * @param input      the object whose state shall be verified
   * @param stateQuery function to query the state of `input`;
   *                   exceptions of type {@link IgnorableStateQueryException} on {@link Function#apply(Object)}
   *                   will be ignored unless a timeout is met; all other exceptions will immediately abort
   *                   waiting; if the query implements {@link SelfDescribing} its description will be used on
   *                   failure reporting
   * @param matcher    validate if the state matches the expectations
   * @param <F>        the type of the object to validate
   * @param <T>        the type of the state query result
   * @return the result of the stateQuery if successfully matched
   */
  <F, T> T until(@Nonnull F input, @Nonnull Function<? super F, T> stateQuery, @Nonnull Matcher<? super T> matcher);
}

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
   * @param input      the input to pass to the query function; if implementing {@link SelfDescribing} the
   *                   description of input will be queried on failure
   * @param stateQuery the function to query the state of input; if implementing {@link SelfDescribing} the
   *                   description of stateQuery will be queried on failure
   * @param matcher    the matcher to validate the result of the query; {@code null} to match any returned value
   * @param <F>        the input type
   * @param <T>        the return type of the state query
   */
  <F, T> T until(@Nonnull F input,
                 @Nonnull Function<? super F, T> stateQuery,
                 @Nullable Matcher<? super T> matcher);

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

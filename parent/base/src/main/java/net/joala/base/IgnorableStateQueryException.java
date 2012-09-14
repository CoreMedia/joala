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
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * Exception which evaluated functions in {@link Wait#until(Object, Function, Matcher)}
 * might throw in order to signal that the current state query failed but should be retried.
 * </p>
 * <p>
 * If the function implements {@link SelfDescribing} its description will be added to the failure message
 * or taken as failure message if no extra message is provided. Otherwise its {@link Object#toString()} method
 * will be called.
 * </p>
 *
 * @see Wait
 * @since 9/14/12
 */
public class IgnorableStateQueryException extends RuntimeException {
  @Nonnull
  private final Function<?, ?> stateQuery;

  /**
   * <p>
   * Constructor with the query which raised the error.
   * </p>
   *
   * @param stateQuery query raising this exception
   */
  public IgnorableStateQueryException(@Nonnull final Function<?, ?> stateQuery) {
    super();
    checkNotNull(stateQuery, "Query function must not be null");
    this.stateQuery = stateQuery;
  }

  /**
   * <p>
   * Constructor with query and cause.
   * </p>
   *
   * @param stateQuery query raising this exception
   * @param cause      exception which caused the failure on state query
   */
  public IgnorableStateQueryException(@Nonnull final Function<?, ?> stateQuery, final Throwable cause) {
    super(cause);
    checkNotNull(stateQuery, "Query function must not be null");
    this.stateQuery = stateQuery;
  }

  /**
   * <p>
   * Constructor with query and message.
   * </p>
   *
   * @param stateQuery query raising this exception
   * @param message    message for this failure, will be enriched by the description of the stateQuery
   */
  public IgnorableStateQueryException(@Nonnull final Function<?, ?> stateQuery, final String message) {
    super(message);
    checkNotNull(stateQuery, "Query function must not be null");
    this.stateQuery = stateQuery;
  }

  /**
   * <p>
   * Constructor with query, message and cause.
   * </p>
   *
   * @param stateQuery query raising this exception
   * @param message    message for this failure, will be enriched by the description of the stateQuery
   * @param cause      exception which caused the failure on state query
   */
  public IgnorableStateQueryException(@Nonnull final Function<?, ?> stateQuery, final String message, final Throwable cause) {
    super(message, cause);
    checkNotNull(stateQuery, "Query function must not be null");
    this.stateQuery = stateQuery;
  }

  @Override
  public String getMessage() {
    return buildMessage(stateQuery, super.getMessage());
  }

  @Override
  public String getLocalizedMessage() {
    return buildMessage(stateQuery, super.getLocalizedMessage());
  }

  /**
   * <p>
   * Build a message combining the original message (if any) with a description of the stateQuery.
   * </p>
   *
   * @param function function which queried the state
   * @param message  message provided for exception; might be {@code null}
   * @return a complete message for the exception
   */
  private static String buildMessage(@Nonnull final Function<?, ?> function, @Nullable final String message) {
    final Description description = new StringDescription();
    if (message == null) {
      description.appendText("Failure: ");
    } else {
      description.appendText(message);
      description.appendText(" - ");
    }
    appendDescriptionOf(description, function);
    return description.toString();
  }

  /**
   * <p>
   * Adds the description of the stateQuery. If the query implements {@link SelfDescribing} it will be asked for
   * its description. Otherwise its {@link Object#toString()} method will be called.
   * </p>
   *
   * @param description description to add the description of the query to
   * @param function    query to derive its description from
   */
  private static void appendDescriptionOf(@Nonnull final Description description, @Nonnull final Function<?, ?> function) {
    if (function instanceof SelfDescribing) {
      description.appendDescriptionOf((SelfDescribing) function);
    } else {
      description.appendValue(function);
    }
  }
}

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
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.joala.condition.util.DescriptionUtil.describeTo;

/**
 * <p>
 * Exception which evaluated functions might throw in order to signal that the current state query failed but should
 * be retried.
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
  private static final String CHECK_QUERY_FUNCTION_MUST_NOT_BE_NULL = "Query function must not be null";
  @NonNull
  private final Function<?, ?> stateQuery;

  /**
   * <p>
   * Constructor with the query which raised the error.
   * </p>
   *
   * @param stateQuery query raising this exception
   */
  public IgnorableStateQueryException(@NonNull final Function<?, ?> stateQuery) {
    super();
    checkNotNull(stateQuery, CHECK_QUERY_FUNCTION_MUST_NOT_BE_NULL);
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
  public IgnorableStateQueryException(@NonNull final Function<?, ?> stateQuery, final Throwable cause) {
    super(cause);
    checkNotNull(stateQuery, CHECK_QUERY_FUNCTION_MUST_NOT_BE_NULL);
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
  public IgnorableStateQueryException(@NonNull final Function<?, ?> stateQuery, final String message) {
    super(message);
    checkNotNull(stateQuery, CHECK_QUERY_FUNCTION_MUST_NOT_BE_NULL);
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
  public IgnorableStateQueryException(@NonNull final Function<?, ?> stateQuery, final String message, final Throwable cause) {
    super(message, cause);
    checkNotNull(stateQuery, CHECK_QUERY_FUNCTION_MUST_NOT_BE_NULL);
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
  private static String buildMessage(@NonNull final Function<?, ?> function, @Nullable final String message) {
    final Description description = new StringDescription();
    if (message == null) {
      description.appendText("Failure: ");
    } else {
      description.appendText(message);
      description.appendText(" - ");
    }
    describeTo(description, function);
    return description.toString();
  }

}

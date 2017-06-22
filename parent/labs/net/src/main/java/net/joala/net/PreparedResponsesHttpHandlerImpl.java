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

package net.joala.net;

import com.google.common.base.MoreObjects;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkState;

/**
 * <p>
 * Implementation of {@link PreparedResponsesHttpHandler}.
 * </p>
 *
 * @since 10/4/12
 */
class PreparedResponsesHttpHandlerImpl implements PreparedResponsesHttpHandler {
  private final Deque<Response> responses = new LinkedList<Response>();

  @Override
  public void feedResponses(final Response... responses) {
    this.responses.addAll(Arrays.asList(responses));
  }

  @Override
  public void clearResponses() {
    this.responses.clear();
  }

  @Override
  public int availableResponses() {
    return responses.size();
  }

  @Override
  public void handle(final HttpExchange exchange) throws IOException {
    checkState(!responses.isEmpty(), "No responses available anymore.");
    responses.pollFirst().write(exchange);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("responses", responses)
            .toString();
  }
}

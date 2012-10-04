package net.joala.lab.net;

import com.google.common.base.Objects;
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
    return Objects.toStringHelper(this)
            .add("responses", responses)
            .toString();
  }
}

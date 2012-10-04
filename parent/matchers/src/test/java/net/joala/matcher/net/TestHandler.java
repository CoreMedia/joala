package net.joala.matcher.net;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkState;

/**
* @since 10/4/12
*/
public class TestHandler implements HttpHandler {
  private final Deque<TestResponse> responses = new LinkedList<TestResponse>();

  public void feedResponses(final TestResponse... responses) {
    this.responses.addAll(Arrays.asList(responses));
  }

  public void clearResponses() {
    this.responses.clear();
  }

  public int availableResponses() {
    return responses.size();
  }

  @Override
  public void handle(final HttpExchange exchange) throws IOException {
    checkState(!responses.isEmpty(), "No responses available anymore.");
    responses.pollFirst().write(exchange);
  }
}

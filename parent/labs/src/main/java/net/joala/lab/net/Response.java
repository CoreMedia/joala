package net.joala.lab.net;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 * <p>
 *   A response provided by a {@link PreparedResponsesHttpHandlerImpl}.
 * </p>
 * @since 10/4/12
 */
public interface Response {
  /**
   * Write a response to the given request.
   * @param exchange request to handle
   * @throws IOException in case of an error
   */
  void write(final HttpExchange exchange) throws IOException;
}

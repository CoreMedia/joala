package net.joala.lab.net;

import com.sun.net.httpserver.HttpHandler;

/**
 * HttpHandler which can be fed with prepared responses.
 *
 * @since 10/4/12
 */
public interface PreparedResponsesHttpHandler extends HttpHandler {
  /**
   * Feed responses.
   *
   * @param responses responses to provide for next requests
   */
  void feedResponses(Response... responses);

  /**
   * Clear possibly remaining responses.
   */
  void clearResponses();

  /**
   * <p>
   * Get the number of available responses.
   * </p>
   *
   * @return number of remaining responses
   */
  int availableResponses();
}

package net.joala.lab.net;

import com.google.common.base.Objects;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 * <p>
 * Send the given response but with some delay.
 * </p>
 *
 * @since 10/4/12
 */
public class DelayedResponse implements Response {
  /**
   * Milliseconds to delay the response to be sent.
   */
  private final long delayMillis;
  /**
   * Response to send after delay.
   */
  private final Response response;

  /**
   * <p>
   * Constructor specifying the delay and the response to send.
   * </p>
   *
   * @param delayMillis milliseconds to delay the response to be sent
   * @param response    response to send after delay
   */
  public DelayedResponse(final long delayMillis, final Response response) {
    this.delayMillis = delayMillis;
    this.response = response;
  }

  @Override
  public void write(final HttpExchange exchange) throws IOException {
    try {
      Thread.sleep(delayMillis);
    } catch (InterruptedException ignored) {
    }
    response.write(exchange);
  }

  /**
   * Factory method to create a delayed response.
   *
   * @param delayMillis delay in milliseconds to wait before sending the response
   * @param response the response to send after delay
   * @return response
   */
  public static Response delay(final long delayMillis, final Response response) {
    return new DelayedResponse(delayMillis, response);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("delayMillis", delayMillis)
            .add("response", response)
            .toString();
  }
}

package net.joala.matcher.net;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
* @since 10/4/12
*/
public class DelayedTestResponse extends TestResponse {
  private final long delayMillis;

  public DelayedTestResponse(final long delayMillis, final int responseCode, final String responseBody) {
    super(responseCode, responseBody);
    this.delayMillis = delayMillis;
  }

  @Override
  protected void write(final HttpExchange exchange) throws IOException {
    try {
      Thread.sleep(delayMillis);
    } catch (InterruptedException ignored) {
    }
    super.write(exchange);
  }
}

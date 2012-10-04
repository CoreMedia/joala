package net.joala.lab.net;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

/**
* @since 10/4/12
*/
public class TestResponse {
  private final int responseCode;
  private final String responseBody;

  public TestResponse(final int responseCode, final String responseBody) {
    this.responseCode = responseCode;
    this.responseBody = responseBody;
  }

  protected void write(final HttpExchange exchange) throws IOException {
    exchange.sendResponseHeaders(responseCode, responseBody.length());

    // need GET params here

    final OutputStream os = exchange.getResponseBody();
    try {
      os.write(responseBody.getBytes());
    } finally {
      os.close();
    }
  }
}

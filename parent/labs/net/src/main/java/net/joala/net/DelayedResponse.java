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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.net;

import com.google.common.base.Objects;
import com.sun.net.httpserver.HttpExchange;

import javax.annotation.Nonnull;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

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
  public DelayedResponse(final long delayMillis, @Nonnull final Response response) {
    checkNotNull(response, "Response must not be null.");
    this.delayMillis = delayMillis;
    this.response = response;
  }

  @Override
  public void write(@Nonnull final HttpExchange exchange) throws IOException {
    checkNotNull(exchange, "Exchange must not be null.");
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
   * @param response    the response to send after delay
   * @return response
   */
  public static Response delay(final long delayMillis, @Nonnull final Response response) {
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

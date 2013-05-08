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

package net.joala.internal.net;

import com.sun.net.httpserver.HttpExchange;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * A prepared response which just serves a status code and empty response body.
 * </p>
 *
 * @since 10/4/12
 */
public class StatusCodeResponse implements Response {
  private final int statusCode;

  /**
   * Constructor defining the status code to return.
   *
   * @param statusCode status code
   */
  public StatusCodeResponse(final int statusCode) {
    this.statusCode = statusCode;
  }

  @Override
  public void write(@Nonnull final HttpExchange exchange) throws IOException {
    checkNotNull(exchange, "Exchange must not be null.");
    exchange.sendResponseHeaders(statusCode, 0L);

    final OutputStream os = exchange.getResponseBody();
    os.close();
  }

  /**
   * Factory for status code response.
   *
   * @param statusCode status code to provide on request
   * @return response
   */
  public static Response statusCode(final int statusCode) {
    return new StatusCodeResponse(statusCode);
  }
}

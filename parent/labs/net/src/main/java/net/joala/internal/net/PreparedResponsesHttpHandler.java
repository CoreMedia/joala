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

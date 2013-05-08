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

import java.io.IOException;
import java.net.ServerSocket;

/**
 * <p>
 * Utility methods to deal with ports.
 * </p>
 *
 * @since 10/4/12
 */
public final class PortUtils {
  /**
   * Forbid instantiation.
   */
  private PortUtils() {
  }

  /**
   * Retrieve a free port on the current system.
   *
   * @return free port - if available
   * @throws IOException in case of an error retrieving a free port
   */
  public static int freePort() throws IOException {
    final ServerSocket server = new ServerSocket(0);
    try {
      return server.getLocalPort();
    } finally {
      server.close();
    }
  }
}

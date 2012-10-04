package net.joala.lab.net;

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

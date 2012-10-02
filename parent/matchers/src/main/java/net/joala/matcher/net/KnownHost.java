package net.joala.matcher.net;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import java.net.UnknownHostException;

import static java.net.InetAddress.getAllByName;

/**
 * <p>
 * Validates hostnames if they can be resolved, thus are known to the system.
 * </p>
 *
 * @since 10/2/12
 */
public class KnownHost extends CustomTypeSafeMatcher<String> {
  public KnownHost() {
    super("hostname which can be resolved to IP address");
  }

  @Override
  protected boolean matchesSafely(final String item) {
    try {
      //noinspection ResultOfMethodCallIgnored
      getAllByName(item);
    } catch (UnknownHostException ignored) {
      return false;
    }
    return true;
  }

  /**
   * <p>
   * Validates hostnames if they can be resolved, thus are known to the system.
   * </p>
   *
   * @return hostname validating matcher
   */
  @Factory
  public static Matcher<String> knownHost() {
    return new KnownHost();
  }

}

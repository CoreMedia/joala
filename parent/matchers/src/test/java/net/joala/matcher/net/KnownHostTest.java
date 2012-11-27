package net.joala.matcher.net;

import org.junit.Test;

import static java.net.InetAddress.getByName;
import static net.joala.matcher.net.KnownHost.knownHost;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Tests {@link KnownHost}.
 * </p>
 *
 * @since 10/2/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
public class KnownHostTest {
  // This constant replaces a random value which actually might be a
  // matching host by random. Thus to provide repeatable results the
  // hostname is fixed here. If for some reason this name is ever
  // registered, replace by a different host.
  private static final String UNKNOWN_HOST = "rlfhgjloejh.hvucx.sdfs";

  @Test
  public void knownHost_should_not_match_for_unknown_host() throws Exception {
    assertThat(UNKNOWN_HOST, not(knownHost()));
  }

  @Test
  public void knownHost_should_match_for_known_host() throws Exception {
    assertThat(getByName(null).getHostName(), knownHost());
  }
}

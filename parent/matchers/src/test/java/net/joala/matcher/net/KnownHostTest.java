package net.joala.matcher.net;

import org.junit.Test;

import static java.net.InetAddress.getByName;
import static net.joala.matcher.net.KnownHost.knownHost;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Tests {@link KnownHost}.
 * </p>
 *
 * @since 10/2/12
 */
public class KnownHostTest {

  @Test
  public void knownHost_should_not_match_for_unknown_host() throws Exception {
    assertThat(randomAlphabetic(10), not(knownHost()));
  }

  @Test
  public void knownHost_should_match_for_known_host() throws Exception {
    assertThat(getByName(null).getHostName(), knownHost());
  }
}

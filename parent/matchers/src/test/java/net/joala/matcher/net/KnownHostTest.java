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

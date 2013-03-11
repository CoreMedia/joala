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

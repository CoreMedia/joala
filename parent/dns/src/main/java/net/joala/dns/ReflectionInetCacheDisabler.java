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

package net.joala.dns;

import net.joala.core.reflection.SetAccessibleAction;

import java.lang.reflect.Field;
import java.net.InetAddress;

import static java.security.AccessController.doPrivileged;

/**
 * <p>
 * Disable caching for inet addresses by reflection.
 * </p>
 *
 * @since 10/7/12
 */
class ReflectionInetCacheDisabler {
  /**
   * Class to access by reflection.
   */
  private static final String SUN_NET_INET_ADDRESS_CACHE_POLICY = "sun.net.InetAddressCachePolicy";
  /**
   * Member variable of cache policy with the policy for positive lookups.
   */
  private static final String CACHE_POLICY = "cachePolicy";
  /**
   * Member variable of cache policy with policy for negative lookups.
   */
  private static final String NEGATIVE_CACHE_POLICY = "negativeCachePolicy";
  /**
   * Signals to never cache any entries.
   */
  private static final int CACHE_NEVER = 0;

  /**
   * <p>
   * Try to disable caching by reflection.
   * </p>
   *
   * @throws ReflectionCallException if disabling cache failed
   */
  @SuppressWarnings("unchecked")
  public void disable() throws ReflectionCallException {
    try {
      final Class<?> policyClass = Class.forName(SUN_NET_INET_ADDRESS_CACHE_POLICY, true, InetAddress.class.getClassLoader());
      final Field cachePolicyField = policyClass.getDeclaredField(CACHE_POLICY);
      final Field negativeCachePolicyField = policyClass.getDeclaredField(NEGATIVE_CACHE_POLICY);
      doPrivileged(new SetAccessibleAction(cachePolicyField));
      doPrivileged(new SetAccessibleAction(negativeCachePolicyField));
      cachePolicyField.set(null, CACHE_NEVER);
      negativeCachePolicyField.set(null, CACHE_NEVER);
    } catch (Exception e) { // NOSONAR: We want to be as robust as possible; thus catching all exceptions
      throw new ReflectionCallException(e);
    }
  }
}

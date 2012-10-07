package net.joala.dns;

import java.lang.reflect.Field;
import java.net.InetAddress;

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
  public void disable() throws ReflectionCallException {
    try {
      final Class<?> policyClass = Class.forName(SUN_NET_INET_ADDRESS_CACHE_POLICY, true, InetAddress.class.getClassLoader());
      final Field cachePolicyField = policyClass.getDeclaredField(CACHE_POLICY);
      final Field negativeCachePolicyField = policyClass.getDeclaredField(NEGATIVE_CACHE_POLICY);
      cachePolicyField.setAccessible(true);
      negativeCachePolicyField.setAccessible(true);
      cachePolicyField.set(null, CACHE_NEVER);
      negativeCachePolicyField.set(null, CACHE_NEVER);
    } catch (Exception e) {
      throw new ReflectionCallException(e);
    }
  }
}

package net.joala.dns;

import java.lang.reflect.Field;
import java.net.InetAddress;

/**
 * @since 10/7/12
 */
class ReflectionInetCacheDisabler {
  public void disable() throws ReflectionCallException {
    try {
      final Class<?> policyClass = Class.forName("sun.net.InetAddressCachePolicy", true, InetAddress.class.getClassLoader());
      final Field cachePolicyField = policyClass.getDeclaredField("cachePolicy");
      final Field negativeCachePolicyField = policyClass.getDeclaredField("negativeCachePolicy");
      cachePolicyField.setAccessible(true);
      negativeCachePolicyField.setAccessible(true);
      cachePolicyField.set(null, 0);
      negativeCachePolicyField.set(null, 0);
    } catch (Exception e) {
      throw new ReflectionCallException(e);
    }
  }
}

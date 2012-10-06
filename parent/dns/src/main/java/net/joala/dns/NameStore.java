package net.joala.dns;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.net.InetAddresses;
import org.junit.internal.AssumptionViolatedException;
import org.xbill.DNS.Address;
import org.xbill.DNS.Name;
import org.xbill.DNS.TextParseException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static net.joala.dns.LocalDNSNameServiceDescriptor.NAME_SERVICE_ID;
import static org.junit.Assume.assumeTrue;

/**
 * <p>
 * This store is responsible for overriding calls for resolving host names. It allows you to manually
 * add and remove known hosts during your tests.
 * </p>
 *
 * @since 10/5/12
 */
@ThreadSafe
public final class NameStore {
  private static final String VALIDATION_HOST = NameStore.class.getName() + ".validat.ion";
  private static final InetAddress VALIDATION_IP = InetAddresses.forString("127.0.0.1");
  private Boolean joalaDNSInstalled;
  private static final SystemLogger LOG = SystemLogger.getLogger(NameStore.class);

  private static final Function<String, InetAddress> STRING_TO_INET_ADDRESS = new String2InetAddress();
  private static final String ASSUMPTION_FAILURE_MESSAGE = format("Joala DNS not installed. Please verify that you have set sun.net.spi.nameservice.provider.1=%s before java.net.InetAddress first got loaded. For runtime modification you might call forceJoalaDnsInstalled() instead.", NAME_SERVICE_ID);
  private final Map<Name, Set<InetAddress>> store = Maps.newHashMap();
  private static final NameStore ourInstance = new NameStore();

  private NameStore() {
  }

  @Nonnull
  public static NameStore nameStore() {
    return ourInstance;
  }

  public void forceJoalaDnsInstalled() throws NoSuchFieldException, IllegalAccessException {
    final Field field = InetAddress.class.getDeclaredField("nameService");
    field.setAccessible(true);
    field.set(null, new LocalDNSNameService());
  }

  public void forceCacheDisabled() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
    final Class<?> policyClass = Class.forName("sun.net.InetAddressCachePolicy", true, InetAddress.class.getClassLoader());
    final Field cachePolicyField = policyClass.getDeclaredField("cachePolicy");
    final Field negativeCachePolicyField = policyClass.getDeclaredField("negativeCachePolicy");
    cachePolicyField.set(null, 0);
    negativeCachePolicyField.set(null, 0);
  }

  public void ensureJoalaDnsInstalled() {
    if (Boolean.TRUE.equals(joalaDNSInstalled)) {
      // Save time, don't try again. Assume we are save.
      return;
    } else if (Boolean.FALSE.equals(joalaDNSInstalled)) {
      // Don't try again, we failed once.
      assumeTrue(joalaDNSInstalled);
    }
    registerValidationHost();
    forceJoalaDnsSetup();
    assumeJoalaDnsInstalled();
  }

  private void assumeJoalaDnsInstalled() {
    joalaDNSInstalled = verifyValidationHostResolved();
    if (Boolean.FALSE.equals(joalaDNSInstalled)) {
      throw new AssumptionViolatedException(ASSUMPTION_FAILURE_MESSAGE);
    }
  }

  private void forceJoalaDnsSetup() {
    if (!verifyValidationHostResolved()) {
      try {
        forceJoalaDnsInstalled();
        LOG.warn(format("Forced Joala DNS lookup. This is a hack. For proper usage set JVM system property: sun.net.spi.nameservice.provider.1=%s", NAME_SERVICE_ID));
      } catch (Exception e) {
        LOG.error(format("Failed to force Joala DNS installation. To ensure Joala DNS please set JVM system property sun.net.spi.nameservice.provider.1=%s", NAME_SERVICE_ID), e);
        joalaDNSInstalled = Boolean.FALSE;
      }
      try {
        forceCacheDisabled();
      } catch (Exception e) {
        // see http://docs.oracle.com/javase/7/docs/api/java/net/doc-files/net-properties.html
        LOG.warn("Failed to disable caching. Removing/overriding already resolved hosts will not work. To ensure Joala DNS will be requeried follow instructions to set JVM security policy properties networkaddress.cache.ttl and networkaddress.cache.negative.ttl to 0 (zero).", e);
      }
    }
  }

  private void registerValidationHost() {
    try {
      synchronized (store) {
        store.put(Name.fromString(VALIDATION_HOST), Collections.singleton(VALIDATION_IP));
      }
    } catch (TextParseException e) {
      throw new RuntimeException("Failure in validation host name. Please file a bug report.", e);
    }
  }

  private boolean verifyValidationHostResolved() {
    final InetAddress byName;
    try {
      byName = InetAddress.getByName(VALIDATION_HOST);
    } catch (UnknownHostException ignored) {
      return false;
    }
    return VALIDATION_IP.equals(byName);
  }

  private void register(@Nonnull final Name name, @Nonnull final Collection<InetAddress> addresses) {
    checkNotNull(name, "Name must not be null.");
    checkNotNull(addresses, "Addresses must not be null.");
    checkArgument(!addresses.isEmpty(), "Addresses must not be empty.");
    synchronized (store) {
      if (store.containsKey(name)) {
        final Set<InetAddress> set = store.get(name);
        set.addAll(addresses);
        LOG.info(format("Updated registration for %s to %s.", name, store.get(name)));
      } else {
        store.put(name, Sets.newHashSet(addresses));
        LOG.info(format("Registered %s with %s.", name, store.get(name)));
      }
    }
  }

  public void register(@Nonnull final String name, @Nonnull final InetAddress... addresses) {
    checkNotNull(name, "Name must not be null.");
    try {
      register(Name.fromString(name), Arrays.asList(addresses));
    } catch (TextParseException e) {
      throw new IllegalArgumentException("Unable to parse name.", e);
    }
  }

  public void register(@Nonnull final String name, @Nonnull final String... addresses) {
    checkNotNull(name, "Name must not be null.");
    final List<InetAddress> inetAddresses = Lists.transform(Arrays.asList(addresses), STRING_TO_INET_ADDRESS);
    try {
      register(Name.fromString(name), inetAddresses);
    } catch (TextParseException e) {
      throw new IllegalArgumentException("Unable to parse name.", e);
    }
  }

  public void unregister(@Nonnull final String name) {
    checkNotNull(name, "Name must not be null.");
    try {
      synchronized (store) {
        if (null != store.remove(Name.fromString(name))) {
          LOG.info(format("Unregistered %s.", name));
        }
      }
    } catch (TextParseException e) {
      throw new IllegalArgumentException("Invalid name.", e);
    }
  }

  public void clear() {
    synchronized (store) {
      store.clear();
      LOG.info("Cleared name store.");
    }
  }

  @Nullable
  Name reverseLookup(@Nullable final InetAddress inetAddress) {
    synchronized (store) {
      final Set<Map.Entry<Name, Set<InetAddress>>> entries = store.entrySet();
      for (final Map.Entry<Name, Set<InetAddress>> entry : entries) {
        if (entry.getValue().contains(inetAddress)) {
          return entry.getKey();
        }
      }
    }
    return null;
  }

  @Nonnull
  InetAddress[] lookup(@Nonnull final String name) {
    checkNotNull(name, "Name must not be null.");
    final Name someName;
    try {
      someName = Name.fromString(name);
    } catch (TextParseException e) {
      throw new IllegalArgumentException("Unable to parse name.", e);
    }
    return lookup(someName);
  }

  @Nonnull
  InetAddress[] lookup(@Nonnull final Name name) {
    checkNotNull(name, "Name must not be null.");
    synchronized (store) {
      if (store.containsKey(name)) {
        LOG.info(format("Succeeded to lookup %s.", name));
        return Iterables.toArray(store.get(name), InetAddress.class);
      }
    }
    LOG.info(format("Failed to lookup %s.", name));
    return new InetAddress[0];
  }

  private static class String2InetAddress implements Function<String, InetAddress> {
    @Override
    @Nonnull
    public InetAddress apply(@Nullable final String input) {
      try {
        checkNotNull(input, "Address must not be null.");
        return Address.getByAddress(input);
      } catch (UnknownHostException e) {
        throw new IllegalArgumentException("Failed to create InetAddress.", e);
      }
    }
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("store", store)
            .toString();
  }
}

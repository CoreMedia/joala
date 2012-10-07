package net.joala.dns;

import com.google.common.collect.ImmutableList;
import org.xbill.DNS.Name;
import org.xbill.DNS.spi.DNSJavaNameServiceDescriptor;
import sun.net.spi.nameservice.NameService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static net.joala.dns.FallbackInetAddressNameService.fallbackNameService;

/**
 * <p>
 * Central class for Joala DNS. It accesses the {@link NameStore} to resolve host entries which are specified
 * by your tests. If this fails (which is normal for all unmodified host entries) several fallback strategies
 * are applied. For more information and how to do better in JVM 7+ please see the {@link net.joala.dns package
 * documentation}.
 * </p>
 *
 * @since 10/5/12
 * @see net.joala.dns package documentation
 */
public class LocalDNSNameService implements NameService {
  /**
   * A logger using System-PrintStreams.
   */
  private static final SystemLogger LOG = SystemLogger.getLogger(LocalDNSNameService.class);
  /**
   * List of fallback name services to use.
   */
  private static final ImmutableList<NameService> FALLBACKS;
  /**
   * Separator for fallback strategies.
   */
  private static final String FALLBACK_SEPARATOR_CHAR = ",";
  /**
   * Pattern to separate fallback strategy ids.
   */
  private static final Pattern FALLBACK_SEPARATOR = Pattern.compile(FALLBACK_SEPARATOR_CHAR);
  /**
   * Fallback ID for DNS Java.
   */
  private static final String FALLBACK_DNSJAVA_ID = "dnsjava";
  /**
   * Fallback ID for using JVM Default.
   */
  private static final String FALLBACK_DEFAULT_ID = "default";
  /**
   * Default fallback strategies to use.
   */
  private static final String FALLBACK_DEFAULTS = FALLBACK_DNSJAVA_ID + FALLBACK_SEPARATOR_CHAR + FALLBACK_DEFAULT_ID;
  /**
   * Property to set to control fallback strategies.
   */
  private static final String FALLBACKS_PROPERTY = "net.joala.dns.fallbacks";

  /**
   * Setup fall back nameservices.
   */
  static {
    final String requestedFallbacksValue = System.getProperty(FALLBACKS_PROPERTY, FALLBACK_DEFAULTS);
    final String[] fallbacks = FALLBACK_SEPARATOR.split(requestedFallbacksValue);
    final ImmutableList.Builder<NameService> builder = ImmutableList.builder();
    for (final String fallback : fallbacks) {
      addFallback(fallback, builder);
    }
    FALLBACKS = builder.build();
    if (FALLBACKS.isEmpty()) {
      LOG.info("Joala DNS Fallbacks disabled.");
    }
  }

  /**
   * <p>
   * Add fallback as requested by id. Surrounding spaces and case is ignored. Unknown fallbacks are silently
   * ignored so that you could for example specify "disabled" in order to have no fallbacks at all.
   * </p>
   *
   * @param id      the id of the fallback; supported {@code dnsjava} and {@code default}
   * @param builder builder to add the fallback instances to
   */
  private static void addFallback(@Nonnull final String id, @Nonnull final ImmutableList.Builder<NameService> builder) {
    try {
      NameService service = null;
      if (FALLBACK_DNSJAVA_ID.equalsIgnoreCase(id.trim())) {
        service = new DNSJavaNameServiceDescriptor().createNameService();
      } else if (FALLBACK_DEFAULT_ID.equalsIgnoreCase(id.trim())) {
        service = fallbackNameService();
      }
      if (service != null) {
        builder.add(service);
        LOG.info(format("Adding Name Service %s as fallback: %s", id, service.getClass()));
      }
    } catch (Exception e) {
      LOG.warn(format("Unable to create fallback service %s. Fallback ignored.", id), e);
    }
  }

  @Override
  @Nonnull
  public InetAddress[] lookupAllHostAddr(@Nonnull final String name) throws UnknownHostException {
    checkNotNull(name, "Hostname must not be null.");
    final InetAddress[] fromStore = NameStore.nameStore().lookup(name);
    if (fromStore.length > 0) {
      return fromStore;
    }
    return fallbackLookupAllHostAddr(name);
  }

  @Override
  @Nonnull
  public String getHostByAddr(@Nonnull final byte[] addr) throws UnknownHostException {
    checkNotNull(addr, "Address must not be null.");
    final Name fromStore = NameStore.nameStore().reverseLookup(InetAddress.getByAddress(addr));
    if (fromStore != null) {
      return fromStore.toString();
    }
    return fallbackGetHostByAddr(addr);
  }

  /**
   * <p>
   * Go through fallbacks to resolve a given hostname.
   * </p>
   *
   * @param name the hostname to resolve
   * @return the address the hostname got resolved to
   * @throws UnknownHostException if the address could not be resolved
   */
  @Nonnull
  private InetAddress[] fallbackLookupAllHostAddr(@Nonnull final String name) throws UnknownHostException {
    InetAddress[] fromFallbacks = null;
    UnknownHostException firstException = null;
    for (final NameService nameService : FALLBACKS) {
      try {
        fromFallbacks = nameService.lookupAllHostAddr(name);
        break;
      } catch (UnknownHostException e) {
        if (firstException == null) {
          firstException = e;
        }
      }
    }
    return handleFallbackResponse(
            fromFallbacks,
            firstException,
            format("Could not find any fallback service which could resolve %s.", name));
  }

  /**
   * <p>
   * Ask fallbacks for host by provided address.
   * </p>
   *
   * @param addr the address to search for
   * @return name of the host with the given address
   * @throws UnknownHostException if such a host could not be found in the fallbacks
   */
  @Nonnull
  private String fallbackGetHostByAddr(@Nonnull final byte[] addr) throws UnknownHostException {
    String fromFallbacks = null;
    UnknownHostException firstException = null;
    for (final NameService nameService : FALLBACKS) {
      try {
        fromFallbacks = nameService.getHostByAddr(addr);
        break;
      } catch (UnknownHostException e) {
        if (firstException == null) {
          firstException = e;
        }
        fromFallbacks = null;
      }
    }
    return handleFallbackResponse(
            fromFallbacks,
            firstException,
            format("Could not find any fallback service which could resolve address %s.", Arrays.toString(addr)));
  }

  /**
   * <p>
   * Handles the provided response from fallbacks.
   * </p>
   *
   * @param fromFallbacks  the answer from the fallbacks; a value of {@code null} causes an {@link UnknownHostException}
   * @param firstException the first exception provided by the fallbacks - if any; will be reused if it is required
   *                       to throw an {@link UnknownHostException}.
   * @param message        the message to add to exception if {@code firstException} is not used
   * @param <T>            the response type
   * @return either the response as provided by fallback or ends with exception
   * @throws UnknownHostException exception if the {@code fromFallbacks} was {@code null}
   */
  @Nonnull
  private <T> T handleFallbackResponse(@Nullable final T fromFallbacks,
                                       @Nullable final UnknownHostException firstException,
                                       @Nonnull final String message) throws UnknownHostException {
    if (fromFallbacks == null) {
      if (firstException == null) {
        throw new UnknownHostException(message);
      } else {
        throw firstException;
      }
    }
    return fromFallbacks;
  }

}

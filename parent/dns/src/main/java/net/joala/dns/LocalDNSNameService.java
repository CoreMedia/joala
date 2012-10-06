package net.joala.dns;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.xbill.DNS.Name;
import org.xbill.DNS.Options;
import org.xbill.DNS.spi.DNSJavaNameServiceDescriptor;
import sun.net.spi.nameservice.NameService;

import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static net.joala.dns.FallbackInetAddressNameService.fallbackNameService;

/**
 * @since 10/5/12
 */
public class LocalDNSNameService implements NameService {
  private static final SystemLogger LOG = SystemLogger.getLogger(LocalDNSNameService.class);
  private static final ImmutableList<NameService> FALLBACKS;

  static {
    final ImmutableList.Builder<NameService> builder = ImmutableList.builder();
    try {
      builder.add(fallbackNameService());
    } catch (InstantiationException e) {
      LOG.warn("Unable to create fallback service. Might fail to resolve hosts from /etc/hosts.", e);
    }
    builder.add(new DNSJavaNameServiceDescriptor().createNameService());
    FALLBACKS = builder.build();
    LOG.info(format("Fallbacks configured to %s.", Lists.<NameService,String>transform(FALLBACKS, new NameServiceToClassname())));
  }

  @Override
  public InetAddress[] lookupAllHostAddr(final String name) throws UnknownHostException {
    final InetAddress[] fromStore = NameStore.nameStore().lookup(name);
    if (fromStore.length > 0) {
      return fromStore;
    }
    return fallbackLookupAllHostAddr(name);
  }

  @Override
  public String getHostByAddr(final byte[] addr) throws UnknownHostException {
    final Name fromStore = NameStore.nameStore().reverseLookup(InetAddress.getByAddress(addr));
    if (fromStore != null) {
      return fromStore.toString();
    }
    return fallbackGetHostByAddr(addr);
  }

  private InetAddress[] fallbackLookupAllHostAddr(final String name) throws UnknownHostException {
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

  private String fallbackGetHostByAddr(final byte[] addr) throws UnknownHostException {
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
      }
    }
    return handleFallbackResponse(
            fromFallbacks,
            firstException,
            format("Could not find any fallback service which could resolve address %s.", Arrays.toString(addr)));
  }

  private <T> T handleFallbackResponse(final T fromFallbacks, final UnknownHostException firstException, final String message) throws UnknownHostException {
    if (fromFallbacks == null) {
      if (firstException == null) {
        throw new UnknownHostException(message);
      } else {
        throw firstException;
      }
    }
    return fromFallbacks;
  }

  private static class NameServiceToClassname implements Function<NameService, String> {
    @Override
    public String apply(@Nullable final NameService input) {
      if (input == null) {
        return "<null>";
      }
      return input.getClass().getName();
    }
  }
}

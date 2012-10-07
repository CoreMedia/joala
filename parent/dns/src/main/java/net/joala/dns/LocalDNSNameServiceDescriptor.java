package net.joala.dns;

import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;

/**
 * <p>
 * Constructs the {@link LocalDNSNameService} and returns references to it. Is referenced from the
 * NameServiceDescriptor in {@code META-INF/services}.
 * </p>
 *
 * @since 10/5/12
 * @see net.joala.dns package documentation
 */
public class LocalDNSNameServiceDescriptor implements NameServiceDescriptor {

  /**
   * Convention. Supported type to report.
   */
  private static final String DNS_TYPE = "dns";
  /**
   * Name of this DNS service.
   */
  private static final String DNS_PROVIDER_NAME = "joala";
  /**
   * Complete ID of this service to specify as system property.
   */
  public static final String NAME_SERVICE_ID = DNS_TYPE + "," + DNS_PROVIDER_NAME;
  /**
   * The name service which will be returned by this descriptor.
   */
  private static final NameService NAME_SERVICE = new LocalDNSNameService();

  @Override
  public NameService createNameService() {
    return NAME_SERVICE;
  }

  @Override
  public String getProviderName() {
    return DNS_PROVIDER_NAME;
  }

  /**
   * <p>
   * Returns this name service type
   * "dns" "nis" etc
   * </p>
   */
  @Override
  public String getType() {
    return DNS_TYPE;
  }
}

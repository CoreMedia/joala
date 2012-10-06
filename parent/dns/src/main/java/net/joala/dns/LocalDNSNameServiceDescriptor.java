package net.joala.dns;

import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;

/**
 * <p>
 * Constructs the LocalManagedDns and returns references to it.
 * </p>
 * <p/>
 * <p>
 * This class assigns the name of this name service provider, which is "dns,LocalManagedDns".
 * </p>
 *
 * @see <a href="http://rkuzmik.blogspot.de/2006/08/local-managed-dns-java_11.html">Roman Kuzmik &ndash; Local Managed DNS (Java); 2006-08-11</a>
 * @since 10/5/12
 */
public class LocalDNSNameServiceDescriptor implements NameServiceDescriptor {

  private static final String DNS_TYPE = "dns";
  private static final String DNS_PROVIDER_NAME = "joala";
  public static final String NAME_SERVICE_ID = DNS_TYPE + "," + DNS_PROVIDER_NAME;
  private static final NameService NAME_SERVICE = new LocalDNSNameService();

  @Override
  public NameService createNameService() throws Exception {
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

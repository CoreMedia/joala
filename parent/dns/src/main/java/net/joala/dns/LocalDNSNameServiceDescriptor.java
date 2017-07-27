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
 * @deprecated Will be removed soon.
 */
@SuppressWarnings("deprecation")
@Deprecated
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

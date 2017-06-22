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

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.net.InetAddresses;
import org.junit.internal.AssumptionViolatedException;
import sun.net.spi.nameservice.NameService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static java.lang.String.format;
import static net.joala.dns.LocalDNSNameServiceDescriptor.NAME_SERVICE_ID;
import static org.junit.Assume.assumeTrue;

/**
 * <p>
 * A workaround if required system properties have not been set. This class tries to
 * install Joala DNS via reflection. This approach is error prone as it strongly
 * relies on the JVM implementation which is private API.
 * </p>
 *
 * @since 10/7/12
 */
class LocalDNSNameServiceEnforcer {
  /**
   * A logger using System-PrintStreams.
   */
  private static final SystemLogger LOG = SystemLogger.getLogger(LocalDNSNameServiceEnforcer.class);
  /**
   * Host name to validate if Joala DNS is up and running.
   */
  private static final String VALIDATION_HOST = "joala.dns.validat.ion";
  /**
   * IP Address used for validation.
   */
  private static final InetAddress VALIDATION_IP = InetAddresses.forString("127.0.0.1");
  /**
   * Installer strategies for DNS name service to try. Strategies are adopted to the several supported JVM versions.
   */
  private static final List<ReflectionNameServiceInstaller> REFLECTION_NAME_SERVICE_INSTALLERS;
  /**
   * Strategy to enforce the cache to be disabled. Might change to a list of several strategies later.
   */
  private static final ReflectionInetCacheDisabler REFLECTION_INET_CACHE_DISABLER = new ReflectionInetCacheDisabler();
  /**
   * Message to output if Joala DNS could not be enabled.
   */
  private static final String ASSUMPTION_FAILURE_MESSAGE = format(
          "Joala DNS not installed. Please verify that you have set sun.net.spi.nameservice.provider.1=%s " +
                  "before java.net.InetAddress first got loaded. For runtime modification you might call " +
                  "ensureJoalaDnsInstalled() instead.", NAME_SERVICE_ID);

  /**
   * Store used for Joala DNS validation.
   */
  private final NameStore store;
  /**
   * <p>
   * Three state flag to signal if Joala DNS has been detected as installed or not.
   * </p>
   * <ul>
   * <li>{@code true} &ndash; Joala DNS has been detected to be installed</li>
   * <li>{@code false} &ndash; Joala DNS has been detected to not be installed</li>
   * <li>{@code null} &ndash; initial state; state of Joala DNS is unknown</li>
   * </ul>
   */
  private Boolean joalaDNSInstalled;

  /**
   * Set up the different strategies to install the name service.
   */
  static {
    final ImmutableList.Builder<ReflectionNameServiceInstaller> builder = ImmutableList.builder();
    builder.add(new ReflectionJava6NameServiceInstaller());
    builder.add(new ReflectionJava7NameServiceInstaller());
    REFLECTION_NAME_SERVICE_INSTALLERS = builder.build();
  }

  /**
   * Constructor setting the name store to use to control if Joala DNS is installed.
   *
   * @param store name store
   */
  LocalDNSNameServiceEnforcer(final NameStore store) {
    this.store = store;
  }

  /**
   * <p>
   * Ensures/verifies if Joala DNS is installed. Throws a
   * {@link AssumptionViolatedException} if Joala could or is
   * not installed. Thus may be used in your tests to skip
   * further execution.
   * </p>
   */
  public void ensureJoalaDnsInstalled() {
    if (Boolean.TRUE.equals(joalaDNSInstalled)) {
      // Save time, don't try again. Assume we are save.
      return;
    } else if (Boolean.FALSE.equals(joalaDNSInstalled)) {
      // Don't try again, we failed once.
      assumeTrue(joalaDNSInstalled);
    }
    forceJoalaDnsSetup();
    assumeJoalaDnsInstalled();
  }

  /**
   * <p>
   * If required enforces Joala DNS to be installed via reflection.
   * </p>
   */
  private void forceJoalaDnsSetup() {
    if (!verifyValidationHostResolved()) {
      forceJoalaDnsInstalled();
      forceCacheDisabled();
    }
  }

  /**
   * <p>
   * Validate if Joala is installed and end with an assumption failure
   * if it could not be installed.
   * </p>
   */
  private void assumeJoalaDnsInstalled() {
    joalaDNSInstalled = verifyValidationHostResolved();
    if (Boolean.FALSE.equals(joalaDNSInstalled)) {
      throw new AssumptionViolatedException(ASSUMPTION_FAILURE_MESSAGE);
    }
  }

  /**
   * Tries with different strategies to install Joala DNS as name service.
   */
  private void forceJoalaDnsInstalled() {
    final NameService nameService = new LocalDNSNameServiceDescriptor().createNameService();
    boolean passed = false;
    for (final ReflectionNameServiceInstaller installer : REFLECTION_NAME_SERVICE_INSTALLERS) {
      try {
        installer.install(nameService);
        passed = true;
        break;
      } catch (ReflectionCallException e) {
        LOG.info("Failed to force Joala DNS installation. Will retry with next strategy.", e);
      }
    }
    if (!passed) {
      LOG.warn("Failed to install name service by reflection. Please use the name provider system properties instead.");
    }
  }

  /**
   * Force to disable cache.
   */
  private void forceCacheDisabled() {
    try {
      REFLECTION_INET_CACHE_DISABLER.disable();
    } catch (ReflectionCallException e) {
      LOG.info("Failed to disable DNS Cache. Please use appropriate policy file.", e);
    }
  }

  /**
   * <p>
   * Validate by try and error if Joala DNS is installed or not.
   * </p>
   *
   * @return {@code true} if Joala DNS is detected to be installed; {@code false} if not
   */
  private boolean verifyValidationHostResolved() {
    final InetAddress byName;
    final String host = registerValidationHost();
    try {
      byName = InetAddress.getByName(host);
    } catch (UnknownHostException ignored) {
      return false;
    } finally {
      store.unregister(host);
    }
    return VALIDATION_IP.equals(byName);
  }

  /**
   * <p>
   * Register some random host in order to use it for validation of the DNS service.
   * </p>
   *
   * @return registered host name
   */
  private String registerValidationHost() {
    // Require to add random hosts in case cashing is enabled.
    final String validationHostName = VALIDATION_HOST + "." + System.currentTimeMillis();
    store.register(validationHostName, VALIDATION_IP);
    return validationHostName;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("store", store)
            .add("joalaDNSInstalled", joalaDNSInstalled)
            .toString();
  }
}

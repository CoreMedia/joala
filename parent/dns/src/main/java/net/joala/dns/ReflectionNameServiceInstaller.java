package net.joala.dns;

import sun.net.spi.nameservice.NameService;

import javax.annotation.Nonnull;

/**
 * <p>
 * Base class for name service installers used as fallback if the name service has not
 * been installed in the standard Java way.
 * </p>
 *
 * @since 10/7/12
 */
abstract class ReflectionNameServiceInstaller {
  /**
   * Installs the given name service.
   *
   * @param nameService name service to install
   * @throws ReflectionCallException raised if installing the name service by reflection failed.
   */
  protected abstract void install(@Nonnull final NameService nameService) throws ReflectionCallException;
}

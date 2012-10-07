package net.joala.dns;

import sun.net.spi.nameservice.NameService;

/**
 * @since 10/7/12
 */
abstract class ReflectionNameServiceInstaller {
  protected abstract void install(final NameService nameService) throws ReflectionCallException;
}

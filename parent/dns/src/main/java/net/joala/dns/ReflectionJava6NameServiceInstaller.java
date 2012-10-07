package net.joala.dns;

import sun.net.spi.nameservice.NameService;

import java.lang.reflect.Field;
import java.net.InetAddress;

/**
 * @since 10/7/12
 */
class ReflectionJava6NameServiceInstaller extends ReflectionNameServiceInstaller {
  @Override
  protected void install(final NameService nameService) throws ReflectionCallException {
    try {
      final Field field = InetAddress.class.getDeclaredField("nameService");
      field.setAccessible(true);
      field.set(null, nameService);
    } catch (Exception e) {
      throw new ReflectionCallException(e);
    }
  }
}

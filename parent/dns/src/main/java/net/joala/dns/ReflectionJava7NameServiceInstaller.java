package net.joala.dns;

import sun.net.spi.nameservice.NameService;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @since 10/7/12
 */
class ReflectionJava7NameServiceInstaller extends ReflectionNameServiceInstaller {
  @SuppressWarnings("unchecked")
  @Override
  protected void install(final NameService nameService) throws ReflectionCallException {
    try {
      final Field field = InetAddress.class.getDeclaredField("nameServices");
      field.setAccessible(true);
      final Collection<NameService> fieldValue = (Collection<NameService>) field.get(null);
      final Collection<NameService> newValue = new ArrayList<NameService>(fieldValue.size() + 1);
      newValue.add(nameService);
      newValue.addAll(fieldValue);
      field.set(null, newValue);
    } catch (Exception e) {
      throw new ReflectionCallException(e);
    }
  }
}

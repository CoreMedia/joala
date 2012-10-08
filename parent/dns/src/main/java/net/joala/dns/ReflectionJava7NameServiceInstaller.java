package net.joala.dns;

import sun.net.spi.nameservice.NameService;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.security.AccessController.doPrivileged;

/**
 * <p>
 * Installs Joala DNS as Name Service as it was required for Java 6.
 * </p>
 *
 * @since 10/7/12
 */
class ReflectionJava7NameServiceInstaller extends ReflectionNameServiceInstaller {
  @SuppressWarnings("unchecked")
  @Override
  protected void install(@Nonnull final NameService nameService) throws ReflectionCallException {
    checkNotNull(nameService, "Name Service must not be null");
    try {
      final Field field = InetAddress.class.getDeclaredField("nameServices");
      doPrivileged(new SetAccessibleAction(field));
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

package net.joala.dns;

import net.joala.core.reflection.SetAccessibleAction;
import sun.net.spi.nameservice.NameService;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.net.InetAddress;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.security.AccessController.doPrivileged;

/**
 * <p>
 * Installs Joala DNS as Name Service as it was required for Java 6.
 * </p>
 *
 * @since 10/7/12
 */
class ReflectionJava6NameServiceInstaller extends ReflectionNameServiceInstaller {
  @SuppressWarnings("unchecked")
  @Override
  protected void install(@Nonnull final NameService nameService) throws ReflectionCallException {
    checkNotNull(nameService, "Name Service must not be null");
    try {
      final Field field = InetAddress.class.getDeclaredField("nameService");
      doPrivileged(new SetAccessibleAction(field));
      field.set(null, nameService);
    } catch (Exception e) {
      throw new ReflectionCallException(e);
    }
  }
}

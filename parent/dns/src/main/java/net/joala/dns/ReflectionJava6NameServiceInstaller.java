package net.joala.dns;

import com.google.common.base.Preconditions;
import sun.net.spi.nameservice.NameService;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.net.InetAddress;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * Installs Joala DNS as Name Service as it was required for Java 6.
 * </p>
 *
 * @since 10/7/12
 */
class ReflectionJava6NameServiceInstaller extends ReflectionNameServiceInstaller {
  @Override
  protected void install(@Nonnull final NameService nameService) throws ReflectionCallException {
    checkNotNull(nameService, "Name Service must not be null");
    try {
      final Field field = InetAddress.class.getDeclaredField("nameService");
      field.setAccessible(true);
      field.set(null, nameService);
    } catch (Exception e) {
      throw new ReflectionCallException(e);
    }
  }
}

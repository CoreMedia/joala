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

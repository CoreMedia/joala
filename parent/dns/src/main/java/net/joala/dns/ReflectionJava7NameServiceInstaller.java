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
import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.security.AccessController.doPrivileged;

/**
 * <p>
 * Installs Joala DNS as Name Service as it was required for Java 6.
 * </p>
 *
 * @deprecated Will be removed soon.
 */
@SuppressWarnings("deprecation")
@Deprecated
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

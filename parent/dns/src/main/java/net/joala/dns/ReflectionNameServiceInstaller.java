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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

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

/*
 * Copyright 2013 CoreMedia AG
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

package net.joala.image;

/**
 * <p>
 *   Exception raised by {@link ImageBuilder}.
 * </p>
 * @since 2013-02-19
 */
public class ImageBuilderException extends RuntimeException {
  public ImageBuilderException() {
    super();
  }

  public ImageBuilderException(final Throwable cause) {
    super(cause);
  }

  public ImageBuilderException(final String message) {
    super(message);
  }

  public ImageBuilderException(final String message, final Throwable cause) {
    super(message, cause);
  }
}

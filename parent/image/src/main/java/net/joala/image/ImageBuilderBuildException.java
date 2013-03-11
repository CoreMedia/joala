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
 * Exception on building the image with an {@link ImageBuilder}.
 * </p>
 *
 * @since 2013-02-19
 */
public class ImageBuilderBuildException extends ImageBuilderException {
  public ImageBuilderBuildException() {
    super();
  }

  public ImageBuilderBuildException(final Throwable cause) {
    super(cause);
  }

  public ImageBuilderBuildException(final String message) {
    super(message);
  }

  public ImageBuilderBuildException(final String message, final Throwable cause) {
    super(message, cause);
  }
}

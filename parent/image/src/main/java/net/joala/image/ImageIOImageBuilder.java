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

import net.joala.image.config.ImageType;

import javax.activation.MimeType;

/**
 * @since 2013-02-21
 */
public interface ImageIOImageBuilder<T> extends ImageBuilder<T> {
  @Override
  ImageIOImageBuilder<T> width(int value);

  @Override
  ImageIOImageBuilder<T> height(int value);

  @Override
  ImageIOImageBuilder<T> imageType(ImageType value);

  /**
   * <p>
   * Set the mime-type for the image to create.
   * </p>
   *
   * @param value mime-type
   * @return self-reference
   */
  ImageIOImageBuilder<T> mimeType(MimeType value);

  /**
   * <p>
   * Set the mime-type for the image to create.
   * </p>
   *
   * @param value mime-type
   * @return self-reference
   */
  ImageIOImageBuilder<T> mimeType(String value);
}

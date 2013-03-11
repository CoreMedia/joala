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

package net.joala.data.image;

import net.joala.data.image.config.ImageType;

/**
 * <p>
 * Create images on the fly for different purposes.
 * </p>
 *
 * @param <T> the output type
 * @since 2013-02-19
 */
public interface ImageBuilder<T> {
  /**
   * <p>
   * Sets the width of the image which must be greater than 0.
   * </p>
   *
   * @param value width to set
   * @return self-reference
   */
  ImageBuilder<T> width(int value);

  /**
   * <p>
   * Sets the height of the image which must be greater than 0.
   * </p>
   *
   * @param value width to set
   * @return self-reference
   */
  ImageBuilder<T> height(int value);

  /**
   * <p>
   * Sets the image type.
   * </p>
   *
   * @param value image type
   * @return self-reference
   */
  ImageBuilder<T> imageType(ImageType value);

  /**
   * Creates the image in the given type.
   *
   * @return created image
   */
  T build();
}

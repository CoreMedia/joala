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
 * along with Joala.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.image;

import net.joala.image.config.ImageType;

import javax.activation.MimeType;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;

/**
 * @since 2013-02-21
 */
public interface OutputImageBuilder extends ImageIOImageBuilder<Void> {
  @Override
  OutputImageBuilder width(int value);

  @Override
  OutputImageBuilder height(int value);

  @Override
  OutputImageBuilder imageType(ImageType value);

  @Override
  OutputImageBuilder mimeType(MimeType value);

  @Override
  OutputImageBuilder mimeType(String value);

  /**
   * <p>
   * Set the output which might be of type File, RandomAccessFile or OutputStream.
   * See {@link ImageIO#createImageOutputStream(Object)} for more information.
   * </p>
   *
   * @param value output
   * @return self-reference
   */
  OutputImageBuilder output(Object value);

  /**
   * <p>
   * Write the specified image to the selected output file.
   * </p>
   *
   * @return nothing
   */
  @Override
  @Nullable
  Void build();
}

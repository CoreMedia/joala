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

package net.joala.data.image.config;

import com.google.common.base.Objects;

import java.awt.image.BufferedImage;

/**
 * <p>
 * Image Types as supported by some Image Builders.
 * </p>
 *
 * @since 2013-02-21
 */
public enum ImageType {
  /**
   * <p>
   * 8-bit RGB color components packed into integer pixels.
   * </p>
   *
   * @see BufferedImage#TYPE_INT_RGB
   */
  INT_RGB("8-bit RGB", BufferedImage.TYPE_INT_RGB);

  private final String name;
  private final int type;

  ImageType(final String name, final int type) {
    this.name = name;
    this.type = type;
  }

  public int getType() {
    return type;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("name", name)
            .add("imageType", type)
            .toString();
  }
}

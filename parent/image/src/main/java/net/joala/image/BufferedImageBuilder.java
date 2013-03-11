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

import java.awt.image.BufferedImage;

/**
 * @since 2013-02-21
 */
public interface BufferedImageBuilder extends ImageBuilder<BufferedImage> {
  @Override
  BufferedImageBuilder width(int value);

  @Override
  BufferedImageBuilder height(int value);

  @Override
  BufferedImageBuilder imageType(ImageType value);
}

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

package net.joala.image.impl;

import net.joala.image.BufferedImageBuilder;
import net.joala.image.ImagePainter;
import net.joala.image.config.ImageType;

import javax.annotation.Nullable;
import javax.inject.Named;
import java.awt.image.BufferedImage;

/**
 * <p>
 * Default implementation of the image builder.
 * </p>
 *
 * @since 2013-02-19
 */
@Named
public class DefaultBufferedImageBuilder extends AbstractBufferedImageBuilder implements BufferedImageBuilder {

  @Override
  public BufferedImageBuilder width(final int value) {
    setWidth(value);
    return this;
  }

  @Override
  public BufferedImageBuilder height(final int value) {
    setHeight(value);
    return this;
  }

  @Override
  public BufferedImageBuilder imageType(final ImageType value) {
    setImageType(value);
    return this;
  }

  @Override
  public BufferedImageBuilder imagePainter(@Nullable final ImagePainter painter) {
    setImagePainter(painter);
    return this;
  }

  @Override
  public BufferedImage build() {
    return create();
  }
}

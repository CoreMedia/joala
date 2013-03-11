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

import com.google.common.base.Objects;
import net.joala.image.ImagePainter;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.image.BufferedImage;

import static com.google.common.base.Optional.fromNullable;

/**
 * Abstract implementation for Buffered Image Builders.
 *
 * @since 2013-03-11
 */
abstract class AbstractBufferedImageBuilder extends AbstractImageBuilder {
  /**
   * The painter to use to draw the image. If {@code null} a default will be used.
   */
  @CheckForNull
  private ImagePainter painter;

  /**
   * Set the image painter to use.
   *
   * @param painter image painter; {@code null} to use a default painter
   */
  protected void setImagePainter(@Nullable final ImagePainter painter) {
    this.painter = painter;
  }

  /**
   * Retrieve an image painter. If unset a default painter will be returned.
   *
   * @return image painter
   */
  @Nonnull
  private ImagePainter getImagePainter() {
    return fromNullable(painter).or(new DefaultImagePainter());
  }

  /**
   * Create the buffered image with the provided painter.
   *
   * @return buffered image
   */
  public BufferedImage create() {
    checkDimensions();
    final BufferedImage image = createImage();
    getImagePainter().paint(image);
    return image;
  }

  /**
   * Create the image instance to paint to.
   *
   * @return image instance
   */
  @Nonnull
  private BufferedImage createImage() {
    return new BufferedImage(getWidth(), getHeight(), getImageType().getType());
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("painter", painter)
            .toString();
  }
}

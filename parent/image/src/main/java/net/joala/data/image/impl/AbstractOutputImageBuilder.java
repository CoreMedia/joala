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

package net.joala.data.image.impl;

import net.joala.data.image.BufferedImageBuilder;
import net.joala.data.image.ImageBuilderBuildException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.inject.Inject;
import java.awt.image.RenderedImage;
import java.io.IOException;

import static com.google.common.base.Optional.fromNullable;

/**
 * @since 2013-02-21
 */
abstract class AbstractOutputImageBuilder extends AbstractImageIOImageBuilder {
  @Inject
  @CheckForNull // if not using CDI
  private BufferedImageBuilder imageBuilder;

  private Object output;

  protected final Object getOutput() {
    return output;
  }

  protected final void setOutput(final Object output) {
    this.output = output;
  }

  protected final void writeImage() {
    final RenderedImage image = getImageBuilder()
            .width(getWidth())
            .height(getHeight())
            .imageType(getImageType())
            .build();
    try {
      writeImage(getImageWriter(), image, getOutput());
    } catch (IOException e) {
      throw new ImageBuilderBuildException("IOException while writing image data.", e);
    }
  }

  @SuppressWarnings("TypeMayBeWeakened") // NOSONAR
  @Nonnull
  private BufferedImageBuilder getImageBuilder() {
    return fromNullable(imageBuilder).or(new DefaultBufferedImageBuilder());
  }

  private void writeImage(final ImageWriter imageWriter, final RenderedImage image, final Object output) throws IOException {
    final ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(output);
    imageWriter.setOutput(imageOutputStream);
    try {
      imageWriter.write(image);
    } finally {
      imageWriter.dispose();
      imageOutputStream.flush();
    }
  }

}

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
import net.joala.image.ImageBuilderBuildException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.inject.Inject;
import java.awt.image.RenderedImage;
import java.io.IOException;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract implementation for image builders which create images as file output.
 *
 * @since 2013-02-21
 */
abstract class AbstractOutputImageBuilder extends AbstractImageIOImageBuilder {
  /**
   * The image builder to use to create the images which then will be written
   * to a file.
   */
  @Inject
  @CheckForNull // if not using CDI
  private BufferedImageBuilder imageBuilder;

  /**
   * Output for the image file. See ImageIO:
   * <em>an Object to be used as an output destination, such as a File, writable RandomAccessFile, or OutputStream</em>.
   */
  @CheckForNull
  private Object output;

  /**
   * Get the output as set for the image builder.
   *
   * @return output; {@code null} if unset
   */
  @CheckForNull
  protected final Object getOutput() {
    return output;
  }

  /**
   * Set the output. From ImageIO:
   * <em>an Object to be used as an output destination, such as a File, writable RandomAccessFile, or OutputStream</em>.
   *
   * @param output image target output
   */
  protected final void setOutput(@Nonnull final Object output) {
    checkNotNull(output, "Output must not be null.");
    this.output = output;
  }

  /**
   * Write the image to the configured location.
   *
   * @throws ImageBuilderBuildException if output has not been set
   */
  protected final void writeImage() {
    final Object imageOutput = getOutput();
    if (imageOutput == null) {
      throw new ImageBuilderBuildException("Output has not been set.");
    }
    final RenderedImage image = getImageBuilder()
            .width(getWidth())
            .height(getHeight())
            .imageType(getImageType())
            .build();
    try {
      writeImage(getImageWriter(), image, imageOutput);
    } catch (IOException e) {
      throw new ImageBuilderBuildException("IOException while writing image data.", e);
    }
  }

  /**
   * Get Image Builder to use for writing images. If unset (by DI) a default image builder
   * will be provided.
   *
   * @return image builder
   */
  @SuppressWarnings("TypeMayBeWeakened") // NOSONAR
  @Nonnull
  private BufferedImageBuilder getImageBuilder() {
    return fromNullable(imageBuilder).or(new DefaultBufferedImageBuilder());
  }

  /**
   * Write the image.
   *
   * @param imageWriter image writer to use
   * @param image       image to write
   * @param output      output to use
   * @throws IOException if an error occurs writing the image
   */
  private void writeImage(@Nonnull final ImageWriter imageWriter,
                          @Nonnull final RenderedImage image,
                          @Nonnull final Object output) throws IOException {
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

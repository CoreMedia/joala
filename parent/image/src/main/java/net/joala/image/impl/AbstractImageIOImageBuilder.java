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

import com.google.common.base.MoreObjects;
import net.joala.image.ImageBuilderBuildException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimeType;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.util.Iterator;

import static com.google.common.base.Optional.fromNullable;
import static java.lang.String.format;

/**
 * Abstract implementation for image builders which use ImageIO for image creation.
 *
 * @since 2013-02-21
 */
abstract class AbstractImageIOImageBuilder extends AbstractImageBuilder {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultByteArrayImageBuilder.class);
  /**
   * The mime type of the image. Use default if {@code null}.
   */
  @CheckForNull
  private String mimeType;

  /**
   * Sets the mime type.
   *
   * @param value mime type value; {@code null} to use default value
   */
  protected final void setMimeType(@Nullable final MimeType value) {
    setMimeType(value == null ? null : value.toString());
  }

  /**
   * Sets the mime type.
   *
   * @param value mime type value; {@code null} to use default value
   */
  protected final void setMimeType(@Nullable final String value) {
    mimeType = value;
  }

  /**
   * Get mime type. Returns default if unset.
   *
   * @return mime type
   */
  @Nonnull
  protected final String getMimeType() {
    return fromNullable(mimeType).or(getImageBuilderConfig().getDefaultMimeType());
  }

  /**
   * Provide an image writer for the configured mime type. The first matching image writer
   * will be used if there are multiple registered image writers for the given mime type.
   *
   * @return image writer
   */
  @Nonnull
  protected final ImageWriter getImageWriter() {
    final Iterator<ImageWriter> imageWriterIterator = ImageIO.getImageWritersByMIMEType(getMimeType());
    if (!imageWriterIterator.hasNext()) {
      throw new ImageBuilderBuildException(format("ImageIO: No ImageWriter available for MIME Type '%s'.", getMimeType()));
    }
    final ImageWriter imageWriter = imageWriterIterator.next();
    LOG.debug("Using ImageWriter {} to create image for MIME type '{}'.", imageWriter.getClass().getName(), getMimeType());
    return imageWriter;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("mimeType", mimeType)
            .toString();
  }
}

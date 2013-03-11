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

import net.joala.data.image.ImageBuilderBuildException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimeType;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.util.Iterator;

import static com.google.common.base.Optional.fromNullable;
import static java.lang.String.format;

/**
 * @since 2013-02-21
 */
abstract class AbstractImageIOImageBuilder extends AbstractImageBuilder {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultByteArrayImageBuilder.class);
  @CheckForNull
  private String mimeType;

  protected final void setMimeType(final MimeType value) {
    setMimeType(value.toString());
  }

  protected final void setMimeType(final String value) {
    mimeType = value;
  }

  @Nonnull
  protected final String getMimeType() {
    return fromNullable(mimeType).or(getImageBuilderConfig().getDefaultMimeType());
  }

  protected final ImageWriter getImageWriter() {
    final Iterator<ImageWriter> imageWriterIterator = ImageIO.getImageWritersByMIMEType(getMimeType());
    if (!imageWriterIterator.hasNext()) {
      throw new ImageBuilderBuildException(format("ImageIO: No ImageWriter available for MIME Type '%s'.", getMimeType()));
    }
    final ImageWriter imageWriter = imageWriterIterator.next();
    LOG.debug("Using ImageWriter {} to create image for MIME type '{}'.", imageWriter.getClass().getName(), getMimeType());
    return imageWriter;
  }
}

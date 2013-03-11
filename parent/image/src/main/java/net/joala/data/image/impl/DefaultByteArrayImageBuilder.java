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

import net.joala.data.image.ByteArrayImageBuilder;
import net.joala.data.image.OutputImageBuilder;
import net.joala.data.image.config.ImageType;

import javax.activation.MimeType;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayOutputStream;

import static com.google.common.base.Optional.fromNullable;

/**
 * <p>
 * Creates an image as byte array.
 * </p>
 *
 * @since 2013-02-19
 */
@Named
public class DefaultByteArrayImageBuilder extends AbstractImageIOImageBuilder implements ByteArrayImageBuilder {
  @Inject
  @CheckForNull
  private OutputImageBuilder outputImageBuilder;

  @Override
  public ByteArrayImageBuilder width(final int value) {
    setWidth(value);
    return this;
  }

  @Override
  public ByteArrayImageBuilder height(final int value) {
    setHeight(value);
    return this;
  }

  @Override
  public ByteArrayImageBuilder mimeType(final MimeType value) {
    setMimeType(value);
    return this;
  }

  @Override
  public ByteArrayImageBuilder mimeType(final String value) {
    setMimeType(value);
    return this;
  }

  @Override
  public ByteArrayImageBuilder imageType(final ImageType value) {
    setImageType(value);
    return this;
  }

  @SuppressWarnings("TypeMayBeWeakened") // NOSONAR
  @Nonnull
  private OutputImageBuilder getOutputImageBuilder() {
    return fromNullable(outputImageBuilder).or(new DefaultOutputImageBuilder());
  }

  @Override
  public byte[] build() {
    final ByteArrayOutputStream output = new ByteArrayOutputStream();
    getOutputImageBuilder().width(getWidth())
            .height(getHeight())
            .mimeType(getMimeType())
            .imageType(getImageType())
            .output(output)
            .build();
    return output.toByteArray();
  }

}

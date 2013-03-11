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

package net.joala.image.impl;

import net.joala.image.OutputImageBuilder;
import net.joala.image.config.ImageType;

import javax.activation.MimeType;
import javax.inject.Named;

/**
 * @since 2013-02-21
 */
@Named
public class DefaultOutputImageBuilder extends AbstractOutputImageBuilder implements OutputImageBuilder {
  @Override
  public OutputImageBuilder height(final int value) {
    setHeight(value);
    return this;
  }

  @Override
  public OutputImageBuilder width(final int value) {
    setWidth(value);
    return this;
  }

  @Override
  public OutputImageBuilder mimeType(final MimeType value) {
    setMimeType(value);
    return this;
  }

  @Override
  public OutputImageBuilder mimeType(final String value) {
    setMimeType(value);
    return this;
  }

  @Override
  public OutputImageBuilder output(final Object value) {
    setOutput(value);
    return this;
  }

  @Override
  public OutputImageBuilder imageType(final ImageType value) {
    setImageType(value);
    return this;
  }

  @Override
  public Void build() {
    writeImage();
    return null;
  }

}

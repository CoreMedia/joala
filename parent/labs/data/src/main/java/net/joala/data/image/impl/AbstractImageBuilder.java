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
import net.joala.data.image.config.ImageBuilderConfig;
import net.joala.data.image.config.ImageType;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * @since 2013-02-21
 */
abstract class AbstractImageBuilder {
  @Inject
  @CheckForNull
  private ImageBuilderConfig imageBuilderConfig;

  @CheckForNull
  private Integer width;
  @CheckForNull
  private Integer height;
  @CheckForNull
  private ImageType imageType;

  protected final void setWidth(final int width) {
    checkArgument(width > 0, "Width must be > 0.");
    this.width = width;
  }

  protected final void setHeight(final int height) {
    checkArgument(height > 0, "Height must be > 0.");
    this.height = height;
  }

  protected final void setImageType(final ImageType type) {
    this.imageType = checkNotNull(type);
  }

  protected final int getHeight() {
    return fromNullable(height).or(getImageBuilderConfig().getDefaultHeight());
  }

  protected final int getWidth() {
    return fromNullable(width).or(getImageBuilderConfig().getDefaultWidth());
  }

  @Nonnull
  public final ImageType getImageType() {
    return fromNullable(imageType).or(getImageBuilderConfig().getDefaultImageType());
  }

  /**
   * <p>
   * Retrieve object for default configuration for image builders. If not
   * used within Spring context a singleton instance of {@link ImageBuilderConfig}
   * will be used.
   * </p>
   *
   * @return config object for default settings
   */
  @Nonnull
  protected final ImageBuilderConfig getImageBuilderConfig() {
    return fromNullable(imageBuilderConfig).or(ImageBuilderConfig.getInstance());
  }

  protected final void checkDimensions() {
    if (getWidth() <= 0 || getHeight() <= 0) {
      throw new ImageBuilderBuildException(format("Illegal width and height %d x %d.", getWidth(), getHeight()));
    }
  }
}

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
import net.joala.image.config.ImageBuilderConfig;
import net.joala.image.config.ImageBuilderConfigImpl;
import net.joala.image.config.ImageType;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * <p>
 * Abstract implementation for all image builders. Methods provided especially to be used
 * from fluent interface methods.
 * </p>
 *
 * @since 2013-02-21
 */
abstract class AbstractImageBuilder {
  /**
   * Default configuration for image builders. If not set (for example not available through
   * DI context) a default image builder will be used instead.
   */
  @Inject
  @CheckForNull
  private ImageBuilderConfig imageBuilderConfig;

  /**
   * The width of the image. If not set a default will be used.
   */
  @CheckForNull
  private Integer width;
  /**
   * The height of the image. If not set a default will be used.
   */
  @CheckForNull
  private Integer height;
  /**
   * The type of the image. If not set a default will be used.
   */
  @CheckForNull
  private ImageType imageType;

  /**
   * <p>
   * Set image width.
   * </p>
   *
   * @param width image width
   * @throws IllegalArgumentException if width &lt;= 0
   */
  protected final void setWidth(final int width) {
    checkArgument(width > 0, "Width must be > 0.");
    this.width = width;
  }

  /**
   * <p>
   * Set image height.
   * </p>
   *
   * @param height image height
   * @throws IllegalArgumentException if height &lt;= 0
   */
  protected final void setHeight(final int height) {
    checkArgument(height > 0, "Height must be > 0.");
    this.height = height;
  }

  /**
   * <p>
   * Set image type.
   * </p>
   *
   * @param type image type
   * @throws NullPointerException if type is {@code null}
   */
  protected final void setImageType(final ImageType type) {
    this.imageType = checkNotNull(type);
  }

  /**
   * Get height. Use default if unset.
   *
   * @return image height
   */
  protected final int getHeight() {
    return fromNullable(height).or(getImageBuilderConfig().getDefaultHeight());
  }

  /**
   * Get width. Use default if unset.
   *
   * @return image width
   */
  protected final int getWidth() {
    return fromNullable(width).or(getImageBuilderConfig().getDefaultWidth());
  }

  /**
   * Get image type. Use default if unset.
   *
   * @return image type
   */
  @Nonnull
  public final ImageType getImageType() {
    return fromNullable(imageType).or(getImageBuilderConfig().getDefaultImageType());
  }

  /**
   * <p>
   * Retrieve object for default configuration for image builders. If not
   * used within Spring context a singleton instance of {@link ImageBuilderConfigImpl}
   * will be used.
   * </p>
   *
   * @return config object for default settings
   */
  @Nonnull
  protected final ImageBuilderConfig getImageBuilderConfig() {
    return fromNullable(imageBuilderConfig).or(ImageBuilderConfigImpl.getInstance());
  }

  /**
   * Validate if dimensions make any sense.
   *
   * @throws ImageBuilderBuildException if dimensions don't make sense
   */
  protected final void checkDimensions() {
    if (getWidth() <= 0 || getHeight() <= 0) {
      throw new ImageBuilderBuildException(format("Illegal width and height %d x %d.", getWidth(), getHeight()));
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("height", height)
            .add("imageBuilderConfig", imageBuilderConfig)
            .add("width", width)
            .add("imageType", imageType)
            .toString();
  }
}

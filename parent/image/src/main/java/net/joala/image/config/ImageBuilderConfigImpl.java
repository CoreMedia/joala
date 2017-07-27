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

package net.joala.image.config;

import com.google.common.base.MoreObjects;

/**
 * <p>
 * Counterpart for Spring Configuration to hold especially default values
 * for image generation.
 * </p>
 *
 * @since 2013-02-21
 */
public class ImageBuilderConfigImpl implements ImageBuilderConfig {
  private static final int DEFAULT_WIDTH = 256;
  private static final int DEFAULT_HEIGHT = 256;

  private final int defaultWidth;
  private final int defaultHeight;
  private final ImageType defaultImageType;
  private final String defaultMimeType;

  private ImageBuilderConfigImpl() {
    this(DEFAULT_WIDTH, DEFAULT_HEIGHT, ImageType.INT_RGB, "image/png");
  }

  public ImageBuilderConfigImpl(final int defaultWidth,
                                final int defaultHeight,
                                final ImageType defaultImageType,
                                final String defaultMimeType) {
    this.defaultWidth = defaultWidth;
    this.defaultHeight = defaultHeight;
    this.defaultImageType = defaultImageType;
    this.defaultMimeType = defaultMimeType;
  }

  @Override
  public ImageType getDefaultImageType() {
    return defaultImageType;
  }

  @Override
  public int getDefaultHeight() {
    return defaultHeight;
  }

  @Override
  public String getDefaultMimeType() {
    return defaultMimeType;
  }

  @Override
  public int getDefaultWidth() {
    return defaultWidth;
  }

  @SuppressWarnings("UtilityClassWithoutPrivateConstructor")
  private static final class ImageBuilderConfigHolder {
    private static final ImageBuilderConfig INSTANCE = new ImageBuilderConfigImpl();
  }

  /**
   * Use this if not providing an instance through dependency injection.
   *
   * @return instance of config
   */
  public static ImageBuilderConfig getInstance() {
    return ImageBuilderConfigHolder.INSTANCE;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("defaultImageType", defaultImageType)
            .add("defaultWidth", defaultWidth)
            .add("defaultHeight", defaultHeight)
            .add("defaultMimeType", defaultMimeType)
            .toString();
  }

}

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
import net.joala.data.image.config.ImageType;

import javax.inject.Named;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;

/**
 * <p>
 * Default implementation of the image builder.
 * </p>
 *
 * @since 2013-02-19
 */
@Named
public class DefaultBufferedImageBuilder extends AbstractImageBuilder implements BufferedImageBuilder {
  @Override
  public BufferedImageBuilder width(final int value) {
    setWidth(value);
    return this;
  }

  @Override
  public BufferedImageBuilder height(final int value) {
    setHeight(value);
    return this;
  }

  @Override
  public BufferedImageBuilder imageType(final ImageType value) {
    setImageType(value);
    return this;
  }

  @Override
  public BufferedImage build() {
    checkDimensions();
    final int height = getHeight();
    final int width = getWidth();

    final BufferedImage image = new BufferedImage(width, height, getImageType().getType());
    final Graphics2D g2d = image.createGraphics();
    try {
      final Paint gradientWhiteBlue = new GradientPaint(0, 0, Color.WHITE, width, 0, Color.BLUE);
      final Paint gradientBlueWhite = new GradientPaint(0, 0, Color.BLUE, width, 0, Color.WHITE);
      g2d.setPaint(gradientWhiteBlue);
      g2d.fillRect(0, 0, width, height);
      g2d.setPaint(gradientBlueWhite);
      g2d.fillOval(0, 0, width, height);
    } finally {
      g2d.dispose();
    }
    return image;
  }
}

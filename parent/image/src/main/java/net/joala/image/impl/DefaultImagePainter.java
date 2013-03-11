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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;

/**
 * Default image painter drawing some unspecified image.
 *
 * @since 2013-03-11
 */
public class DefaultImagePainter extends AbstractImagePainter {
  @Override
  protected void paint(final BufferedImage image, final Graphics2D g2d) {
    final int width = image.getWidth();
    final int height = image.getHeight();
    final Paint gradientWhiteBlue = new GradientPaint(0, 0, Color.WHITE, width, 0, Color.BLUE);
    final Paint gradientBlueWhite = new GradientPaint(0, 0, Color.BLUE, width, 0, Color.WHITE);
    g2d.setPaint(gradientWhiteBlue);
    g2d.fillRect(0, 0, width, height);
    g2d.setPaint(gradientBlueWhite);
    g2d.fillOval(0, 0, width, height);
  }
}

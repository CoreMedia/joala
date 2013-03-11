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

import net.joala.image.ImagePainter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Abstract implementation as base for image painters.
 *
 * @since 2013-03-11
 */
public abstract class AbstractImagePainter implements ImagePainter {
  @Override
  public final void paint(final BufferedImage image) {
    final Graphics2D g2d = image.createGraphics();
    try {
      paint(image, g2d);
    } finally {
      g2d.dispose();
    }
  }

  protected abstract void paint(final BufferedImage image, final Graphics2D g2d);
}

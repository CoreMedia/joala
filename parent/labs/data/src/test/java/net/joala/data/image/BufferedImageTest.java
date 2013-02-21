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

package net.joala.data.image;

import net.joala.data.image.impl.DefaultBufferedImageBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * @since 2013-02-21
 */
public class BufferedImageTest {
  @Test
  public void test1() throws Exception {
    final BufferedImageBuilder bufferedImageBuilder = new DefaultBufferedImageBuilder();
    final BufferedImage image = bufferedImageBuilder.build();
    Assert.assertNotNull(image);
  }
}

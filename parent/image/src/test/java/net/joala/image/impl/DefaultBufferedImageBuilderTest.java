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

import net.joala.image.BufferedImageBuilder;
import net.joala.image.ImagePainter;
import net.joala.image.config.ImageType;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests {@link DefaultBufferedImageBuilder}.
 *
 * @since 2013-03-12
 */
public class DefaultBufferedImageBuilderTest {
  private static final int SOME_DIMENSION = 22;
  private final DefaultBufferedImageBuilder imageBuilder = new DefaultBufferedImageBuilder();
  private static final ImageType SOME_IMAGE_TYPE = ImageType.INT_RGB;
  private ImagePainter imagePainter;

  @Before
  public void setUp() throws Exception {
    imagePainter = mock(ImagePainter.class);
  }

  @Test
  public void width_should_set_width_and_return_self_reference() throws Exception {
    final BufferedImageBuilder result = imageBuilder.width(SOME_DIMENSION);
    assertEquals("Width should have been set.", SOME_DIMENSION, imageBuilder.getWidth());
    assertSame("Should have returned self-reference.", imageBuilder, result);
  }

  @Test
  public void height_should_set_height_and_return_self_reference() throws Exception {
    final BufferedImageBuilder result = imageBuilder.height(SOME_DIMENSION);
    assertEquals("Height should have been set.", SOME_DIMENSION, imageBuilder.getHeight());
    assertSame("Should have returned self-reference.", imageBuilder, result);
  }

  @Test
  public void imageType_should_set_type_and_return_self_reference() throws Exception {
    final BufferedImageBuilder result = imageBuilder.imageType(SOME_IMAGE_TYPE);
    assertEquals("Type should have been set.", SOME_IMAGE_TYPE, imageBuilder.getImageType());
    assertSame("Should have returned self-reference.", imageBuilder, result);
  }

  @Test
  public void imagePainter_should_set_painter_and_return_self_reference() throws Exception {
    final BufferedImageBuilder result = imageBuilder.imagePainter(imagePainter);
    assertSame("Painter should have been set.", imagePainter, imageBuilder.getImagePainter());
    assertSame("Should have returned self-reference.", imageBuilder, result);
  }

  @Test
  public void build_should_create_some_buffered_image() throws Exception {
    final BufferedImage build = imageBuilder.build();
    assertThat(build, notNullValue());
  }
}

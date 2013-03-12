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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.awt.image.BufferedImage;

import static net.joala.testlet.ToStringTestlet.toStringTestlet;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Tests {@link AbstractBufferedImageBuilder}.
 *
 * @since 2013-03-11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractBufferedImageBuilder.class)
public class AbstractBufferedImageBuilderTest {
  private AbstractBufferedImageBuilder imageBuilder;
  private ImagePainter imagePainter;

  @Before
  public void setUp() throws Exception {
    imageBuilder = mock(AbstractBufferedImageBuilder.class, new CallsRealMethods());
    imagePainter = mock(ImagePainter.class);
  }

  @Test
  public void image_painter_with_value_set() throws Exception {
    final ImagePainter value = imagePainter;
    imageBuilder.setImagePainter(value);
    assertSame("Image Painter should have been set correctly.", value, imageBuilder.getImagePainter());
  }

  @Test
  public void image_painter_from_default() throws Exception {
    assertThat("Image Painter should have a default.", imageBuilder.getImagePainter(), notNullValue());
  }

  @Test
  public void image_painter_should_be_used_for_image_creation() throws Exception {
    imageBuilder.setImagePainter(imagePainter);
    final BufferedImage image = imageBuilder.create();
    Mockito.verify(imagePainter).paint(image);
  }

  @Test
  public void toString_should_contain_necessary_information() throws Throwable {
    toStringTestlet(imageBuilder)
            .ignoreClassname()
            .fieldsFromClass(AbstractBufferedImageBuilder.class)
            .run();
  }

}

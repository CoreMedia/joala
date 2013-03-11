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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static net.joala.testlet.ToStringTestlet.toStringTestlet;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Tests {@link AbstractImageBuilder}.
 *
 * @since 2013-03-11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractImageBuilder.class)
public class AbstractImageBuilderTest {
  private static final int SOME_IMAGE_DIMENSION = 22;
  private static final int SOME_DEFAULT_IMAGE_DIMENSION = 222;
  private static final ImageType SOME_IMAGE_TYPE = ImageType.INT_RGB;
  private AbstractImageBuilder imageBuilder;

  @Before
  public void setUp() throws Exception {
    final ImageBuilderConfig imageBuilderConfig = mock(ImageBuilderConfig.class);
    imageBuilder = mock(AbstractImageBuilder.class, new CallsRealMethods());
    when(imageBuilder.getImageBuilderConfig()).thenReturn(imageBuilderConfig);
    when(imageBuilderConfig.getDefaultHeight()).thenReturn(SOME_DEFAULT_IMAGE_DIMENSION);
    when(imageBuilderConfig.getDefaultWidth()).thenReturn(SOME_DEFAULT_IMAGE_DIMENSION);
    when(imageBuilderConfig.getDefaultImageType()).thenReturn(SOME_IMAGE_TYPE);
  }

  @Test
  public void width_with_value_set() throws Exception {
    final int value = SOME_IMAGE_DIMENSION;
    imageBuilder.setWidth(value);
    assertEquals("Width should have been set correctly.", value, imageBuilder.getWidth());
  }

  @Test
  public void width_from_default() throws Exception {
    assertEquals("Width should use default.", SOME_DEFAULT_IMAGE_DIMENSION, imageBuilder.getWidth());
  }

  @Test
  public void height_with_value_set() throws Exception {
    final int value = SOME_IMAGE_DIMENSION;
    imageBuilder.setHeight(value);
    assertEquals("Height should have been set correctly.", value, imageBuilder.getHeight());
  }

  @Test
  public void height_from_default() throws Exception {
    assertEquals("Height should use default.", SOME_DEFAULT_IMAGE_DIMENSION, imageBuilder.getHeight());
  }

  @Test
  public void imageType_with_value_set() throws Exception {
    final ImageType value = SOME_IMAGE_TYPE;
    imageBuilder.setImageType(value);
    assertEquals("Image Type should have been set correctly.", value, imageBuilder.getImageType());
  }

  @Test
  public void imageType_from_default() throws Exception {
    assertEquals("Image Type should use default.", SOME_IMAGE_TYPE, imageBuilder.getImageType());
  }

  @Test(expected = ImageBuilderBuildException.class)
  public void checkDimensions_fail_for_all_0() throws Exception {
    when(imageBuilder.getWidth()).thenReturn(0);
    when(imageBuilder.getHeight()).thenReturn(0);
    imageBuilder.checkDimensions();
  }

  @Test(expected = ImageBuilderBuildException.class)
  public void checkDimensions_fail_for_width_0() throws Exception {
    when(imageBuilder.getWidth()).thenReturn(0);
    when(imageBuilder.getHeight()).thenReturn(SOME_IMAGE_DIMENSION);
    imageBuilder.checkDimensions();
  }

  @Test(expected = ImageBuilderBuildException.class)
  public void checkDimensions_fail_for_height_0() throws Exception {
    when(imageBuilder.getWidth()).thenReturn(0);
    when(imageBuilder.getHeight()).thenReturn(SOME_IMAGE_DIMENSION);
    imageBuilder.checkDimensions();
  }

  @Test
  public void checkDimensions_pass_for_all_greater_than_0() throws Exception {
    when(imageBuilder.getWidth()).thenReturn(SOME_IMAGE_DIMENSION);
    when(imageBuilder.getHeight()).thenReturn(SOME_IMAGE_DIMENSION);
    imageBuilder.checkDimensions();
  }

  @Test
  public void toString_should_contain_necessary_information() throws Throwable {
    toStringTestlet(imageBuilder)
            .ignoreClassname()
            .fieldsFromClass(AbstractImageBuilder.class)
            .run();
  }

}

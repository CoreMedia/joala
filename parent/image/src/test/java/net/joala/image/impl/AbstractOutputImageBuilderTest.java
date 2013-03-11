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

import net.joala.image.ImageBuilderBuildException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.imageio.ImageWriter;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static net.joala.testlet.ToStringTestlet.toStringTestlet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Tests {@link AbstractOutputImageBuilder}.
 *
 * @since 2013-03-11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractOutputImageBuilder.class)
public class AbstractOutputImageBuilderTest {
  private AbstractOutputImageBuilder imageBuilder;


  @Before
  public void setUp() throws Exception {
    imageBuilder = mock(AbstractOutputImageBuilder.class, new CallsRealMethods());
  }

  @Test
  public void output_with_value_set() throws Exception {
    final File value = File.createTempFile(AbstractOutputImageBuilder.class.getName(), ".img");
    imageBuilder.setOutput(value);
    assertEquals("Output should have been set correctly.", value, imageBuilder.getOutput());
  }

  @SuppressWarnings("ConstantConditions")
  @Test(expected = NullPointerException.class)
  public void null_output_denied() throws Exception {
    imageBuilder.setOutput(null);
  }

  @Test
  public void write_any_image_to_output() throws Exception {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    imageBuilder.setOutput(outputStream);
    imageBuilder.writeImage();
    assertThat("Some bytes should have been written to output.", outputStream.size(), Matchers.greaterThan(0));
  }

  @Test(expected = ImageBuilderBuildException.class)
  public void fail_to_write_image_if_output_unset() throws Exception {
    imageBuilder.writeImage();
  }

  @Test
  public void fail_with_exception_if_image_output_fails() throws Exception {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    imageBuilder.setOutput(outputStream);
    final IOException toBeThrown = new IOException("Forced failure.");
    doThrow(toBeThrown).when(imageBuilder, "writeImage",
            org.mockito.Matchers.any(ImageWriter.class),
            org.mockito.Matchers.any(RenderedImage.class),
            org.mockito.Matchers.any());
    try {
      imageBuilder.writeImage();
      fail("Exception should have been thrown because write has failed.");
    } catch (ImageBuilderBuildException e) {
      assertSame(toBeThrown, e.getCause());
    }
  }

  @Test
  public void toString_should_contain_necessary_information() throws Throwable {
    toStringTestlet(imageBuilder)
            .ignoreClassname()
            .fieldsFromClass(AbstractOutputImageBuilder.class)
            .run();
  }

}

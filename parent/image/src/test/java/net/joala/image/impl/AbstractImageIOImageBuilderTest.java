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
import net.joala.image.config.ImageBuilderConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.activation.MimeType;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static net.joala.testlet.ToStringTestlet.toStringTestlet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Tests {@link AbstractImageIOImageBuilder}.
 *
 * @since 2013-03-11
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractImageIOImageBuilder.class, ImageIO.class})
public class AbstractImageIOImageBuilderTest {
  private static final String SOME_IMAGE_DEFAULT_MIME_TYPE = "image/png";
  private static final String SOME_IMAGE_MIME_TYPE = "image/jpg";
  private AbstractImageIOImageBuilder imageBuilder;
  private ImageWriter imageWriter;

  @Before
  public void setUp() throws Exception {
    final ImageBuilderConfig imageBuilderConfig = mock(ImageBuilderConfig.class);
    imageBuilder = mock(AbstractImageIOImageBuilder.class, new CallsRealMethods());
    imageWriter = mock(ImageWriter.class);
    when(imageBuilder.getImageBuilderConfig()).thenReturn(imageBuilderConfig);
    when(imageBuilderConfig.getDefaultMimeType()).thenReturn(SOME_IMAGE_DEFAULT_MIME_TYPE);
    mockStatic(ImageIO.class);
  }

  @Test
  public void mimetype_with_value_set_from_string() throws Exception {
    final String value = SOME_IMAGE_MIME_TYPE;
    imageBuilder.setMimeType(value);
    assertEquals("Mime Type should have been set correctly.", value, imageBuilder.getMimeType());
  }

  @Test
  public void mimetype_with_value_set_from_mimetype() throws Exception {
    final MimeType value = new MimeType(SOME_IMAGE_MIME_TYPE);
    imageBuilder.setMimeType(value);
    assertEquals("Mime Type should have been set correctly.", value.toString(), imageBuilder.getMimeType());
  }

  @Test
  public void mimetype_from_default() throws Exception {
    assertEquals("Mime Type should use default.", SOME_IMAGE_DEFAULT_MIME_TYPE, imageBuilder.getMimeType());
  }

  @Test
  public void getImageWriter_for_available_ImageWriter() throws Exception {
    final Iterator<ImageWriter> imageWriterIterator = Collections.singletonList(imageWriter).iterator();
    when(ImageIO.getImageWritersByMIMEType(anyString())).thenReturn(imageWriterIterator);
    final ImageWriter actualImageWriter = imageBuilder.getImageWriter();
    assertSame("ImageWriter should have returned as provided by ImageIO.", imageWriter, actualImageWriter);
  }

  @Test(expected = ImageBuilderBuildException.class)
  public void getImageWriter_for_unavailable_ImageWriter() throws Exception {
    final List<ImageWriter> imageWriters = Collections.emptyList();
    when(ImageIO.getImageWritersByMIMEType(anyString())).thenReturn(imageWriters.iterator());
    imageBuilder.getImageWriter();
  }

  @Test
  public void toString_should_contain_necessary_information() throws Throwable {
    toStringTestlet(imageBuilder)
            .ignoreClassname()
            .fieldsFromClass(AbstractImageIOImageBuilder.class)
            .run();
  }

}

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

package net.joala.data.image.config;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import static net.joala.testlet.ToStringTestlet.toStringTestlet;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link ImageBuilderConfigImpl}.
 *
 * @since 2013-03-11
 */
public class ImageBuilderConfigImplTest {
  @Test
  public void toString_should_contain_necessary_information() throws Throwable {
    final ImageBuilderConfig instance = ImageBuilderConfigImpl.getInstance();
    toStringTestlet(instance).run();
  }

  @Test
  public void defaultConfig_has_valid_width() throws Exception {
    final ImageBuilderConfig instance = ImageBuilderConfigImpl.getInstance();
    final int defaultWidth = instance.getDefaultWidth();
    assertThat(defaultWidth, Matchers.greaterThan(0));
  }

  @Test
  public void defaultConfig_has_valid_height() throws Exception {
    final ImageBuilderConfig instance = ImageBuilderConfigImpl.getInstance();
    final int defaultHeight = instance.getDefaultHeight();
    assertThat(defaultHeight, Matchers.greaterThan(0));
  }

  @Test
  public void defaultConfig_has_valid_image_type() throws Exception {
    final ImageBuilderConfig instance = ImageBuilderConfigImpl.getInstance();
    final ImageType defaultImageType = instance.getDefaultImageType();
    assertThat(defaultImageType, Matchers.notNullValue());
  }

  @Test
  public void defaultConfig_has_valid_mime_type() throws Exception {
    final ImageBuilderConfig instance = ImageBuilderConfigImpl.getInstance();
    final String defaultMimeType = instance.getDefaultMimeType();
    assertThat(defaultMimeType, new IsValidMimeType(defaultMimeType));
    assertThat(defaultMimeType, Matchers.notNullValue());
  }

  private static class IsValidMimeType extends CustomTypeSafeMatcher<String> {
    private final String defaultMimeType;

    private IsValidMimeType(final String defaultMimeType) {
      super("valid mimetype");
      this.defaultMimeType = defaultMimeType;
    }

    @Override
    protected void describeMismatchSafely(final String item, final Description mismatchDescription) {
      super.describeMismatchSafely(item, mismatchDescription);
      mismatchDescription.appendText("raising ");
      try {
        new MimeType(item);
      } catch (MimeTypeParseException e) {
        mismatchDescription.appendValue(e);
      }
    }

    @Override
    protected boolean matchesSafely(final String item) {
      try {
        new MimeType(defaultMimeType);
      } catch (MimeTypeParseException ignored) {
        return false;
      }
      return true;
    }
  }
}

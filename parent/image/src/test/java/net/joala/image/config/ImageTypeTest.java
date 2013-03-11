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

package net.joala.image.config;

import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static net.joala.testlet.ToStringTestlet.toStringTestlet;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Tests {@link ImageType}.
 * </p>
 *
 * @since 2013-03-11
 */
public class ImageTypeTest {
  @Test
  public void toString_should_contain_necessary_information() throws Throwable {
    final ImageType[] values = ImageType.values();
    for (final ImageType value : values) {
      toStringTestlet(value).run();
    }
  }

  @Test
  public void getType_should_return_valid_type() throws Exception {
    final ImageType[] values = ImageType.values();
    final ArrayList<Integer> types = newArrayList();
    for (final ImageType value : values) {
      final int type = value.getType();
      assertThat("Type should be valid.", type, greaterThanOrEqualTo(0));
      assertThat("Type should be unique.", types, not(hasItem(type)));
      types.add(type);
    }

  }
}

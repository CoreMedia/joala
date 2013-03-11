/*
 * Copyright 2012 CoreMedia AG
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

package net.joala.data.random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Tests {@link AbstractRandomNumberProvider}. More functionality covered in {@link RandomNumberProvidersTest}.
 * </p>
 *
 * @see RandomNumberProvidersTest
 * @since 9/17/12
 */
@RunWith(MockitoJUnitRunner.class)
public final class AbstractRandomNumberProviderTest {
  @SuppressWarnings("UnusedDeclaration")
  @Mock
  private RandomNumberType<Integer> numberType;

  @Test
  public void toString_should_contain_number_type_information() throws Exception {
    final AbstractRandomNumberProvider<Integer> numberProvider = new SimpleRandomNumberProvider(numberType);
    final String type = "LoremIpsumType";
    Mockito.when(numberType.toString()).thenReturn(type);
    assertThat("Number type class should be contained in toString().", numberProvider.toString(), containsString(type));
  }

  private static class SimpleRandomNumberProvider extends AbstractRandomNumberProvider<Integer> {
    private SimpleRandomNumberProvider(final RandomNumberType<Integer> numberType1) {
      super(numberType1);
    }
  }
}

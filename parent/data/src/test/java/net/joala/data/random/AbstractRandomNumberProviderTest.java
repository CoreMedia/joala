/*
 * Copyright 2012 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
public class AbstractRandomNumberProviderTest {
  @Mock
  private RandomNumberType<Integer> numberType;

  @Test
  public void toString_should_contain_number_type_information() throws Exception {
    final AbstractRandomNumberProvider<Integer> numberProvider = new AbstractRandomNumberProvider<Integer>(numberType) {
    };
    final String type = "LoremIpsumType";
    Mockito.when(numberType.toString()).thenReturn(type);
    assertThat("Number type class should be contained in toString().", numberProvider.toString(), containsString(type));
  }
}

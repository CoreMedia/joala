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

import net.joala.data.DataProvider;
import net.joala.data.DataProvidingException;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Random;

import static org.junit.Assert.assertSame;

/**
 * <p>
 * Tests {@link AbstractRandomDataProvider}.
 * </p>
 *
 * @since 10/1/12
 */
public class AbstractRandomDataProviderTest {
  private static final Integer SOME_INTEGER = new Random().nextInt();

  @Test
  public void fixate_should_return_self_reference() throws Exception {
    final RandomDataProvider<Integer> dataProvider = new SimpleRandomDataProvider();
    assertSame(dataProvider, dataProvider.fixate());
  }

  @Test
  public void fixate_should_return_data_provider_interface() throws Exception {
    final Method method = AbstractRandomDataProvider.class.getMethod("fixate");
    assertSame(DataProvider.class, method.getReturnType());
  }

  private static final class SimpleRandomDataProvider extends AbstractRandomDataProvider<Integer> {
    @Override
    public Integer get() throws DataProvidingException {
      return SOME_INTEGER;
    }
  }

}

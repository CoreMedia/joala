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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.newdata.random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Random;

import static net.joala.testlet.ToStringTestlet.toStringTestlet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * <p>
 * Tests {@link RandomProvider}.
 * </p>
 *
 * @since 10/24/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
@RunWith(PowerMockRunner.class)
@PrepareForTest(RandomProvider.class)
public class RandomProviderTest {
  private static final Long SOME_SEED = 4242L;

  @Mock
  private Random random;
  @Captor
  ArgumentCaptor<Long> seedCaptor;

  @Test
  public void default_constructor_enables_provider_to_create_valid_randoms() throws Exception {
    whenNew(Random.class).withArguments(seedCaptor.capture()).thenReturn(random);
    final Random providedRandom = new RandomProvider().get();
    verifyNew(Random.class);
    assertSame("Expected random object got created.", random, providedRandom);
    assertNotNull("Random number should have been created with a seed.", seedCaptor.getValue());
  }

  @Test
  public void onearg_constructor_with_null_enables_provider_to_create_valid_randoms() throws Exception {
    whenNew(Random.class).withArguments(seedCaptor.capture()).thenReturn(random);
    final Random providedRandom = new RandomProvider(null).get();
    verifyNew(Random.class);
    assertSame("Expected random object got created.", random, providedRandom);
    assertNotNull("Random number should have been created with a seed.", seedCaptor.getValue());
  }

  @Test
  public void defined_seed_should_be_used_for_random_creation() throws Exception {
    whenNew(Random.class).withArguments(seedCaptor.capture()).thenReturn(random);
    final Random providedRandom = new RandomProvider(SOME_SEED).get();
    verifyNew(Random.class);
    assertSame("Expected random object got created.", random, providedRandom);
    assertEquals("Defined seed should have been used.", SOME_SEED, seedCaptor.getValue());
  }

  @Test
  public void toString_for_noarg_constructor_should_meet_requirements() throws Throwable {
    toStringTestlet(new RandomProvider()).run();
  }

  @Test
  public void toString_for_onearg_constructor_should_meet_requirements() throws Throwable {
    toStringTestlet(new RandomProvider(SOME_SEED)).run();
  }

}

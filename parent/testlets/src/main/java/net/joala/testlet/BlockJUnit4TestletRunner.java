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

package net.joala.testlet;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.junit.Ignore;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * <p>
 * Runner which gets instantiated by {@link AbstractTestlet} in order to run the testlet.
 * </p>
 *
 * @since 10/4/12
 */
final class BlockJUnit4TestletRunner extends BlockJUnit4ClassRunner {
  private final Object testlet;

  /**
   * <p>
   * Set the testlet to run.
   * </p>
   *
   * @param testlet testlet to run
   * @throws InitializationError forwarded from {@link BlockJUnit4ClassRunner#BlockJUnit4ClassRunner(Class)}
   */
  BlockJUnit4TestletRunner(@Nonnull final Object testlet) throws InitializationError {
    super(Preconditions.checkNotNull(testlet).getClass());
    this.testlet = testlet;
  }

  /**
   * <p>
   * Actually instead of creating the test just returns the prepared testlet.
   * </p>
   *
   * @return testlet
   */
  @Override
  protected Object createTest() {
    return testlet;
  }

  /**
   * Validation of constructor disabled.
   *
   * @param errors unused
   */
  @Override
  protected void validateConstructor(final List<Throwable> errors) {
    // don't validate
  }

  /**
   * <p>
   * Any class level annotations like {@link Ignore} are ignored.
   * </p>
   *
   * @return empty list
   */
  @Override
  protected Annotation[] getRunnerAnnotations() {
    return new Annotation[0];
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("testlet", testlet)
            .toString();
  }
}

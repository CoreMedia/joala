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

package net.joala.expression;

import com.google.common.base.Objects;
import org.hamcrest.Description;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * Convenience abstract implementation of {@link Expression} which can get
 * a simple description via constructor. Thus you can skip declaring the method
 * {@link #describeTo(Description)}.
 * </p>
 *
 * @param <T> the result type of the expression
 * @since 2/27/12
 */
public abstract class AbstractExpression<T> implements Expression<T> {
  /**
   * The simple description.
   */
  @Nullable
  private final String simpleDescription;

  protected AbstractExpression() {
    this(null);
  }

  /**
   * <p>
   * Constructor with a simple description.
   * </p>
   *
   * @param simpleDescription description to add; {@code null} if this object shall not provide any description
   */
  protected AbstractExpression(@Nullable final String simpleDescription) {
    this.simpleDescription = simpleDescription;
  }

  /**
   * <p>
   * Will add the provided simple description to the provided description.
   * </p>
   *
   * @param description The description to be built or appended to.
   */
  @Override
  public void describeTo(@Nonnull final Description description) {
    checkNotNull(description, "Description must not be null.");
    if (simpleDescription != null) {
      description.appendText(simpleDescription);
    }
  }

  /**
   * <p>
   * String representation for debugging purpose only.
   * </p>
   *
   * @return string representation
   */
  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("simpleDescription", simpleDescription)
            .toString();
  }
}

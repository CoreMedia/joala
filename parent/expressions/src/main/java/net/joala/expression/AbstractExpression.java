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

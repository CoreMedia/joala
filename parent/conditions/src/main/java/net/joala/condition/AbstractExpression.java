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

package net.joala.condition;

import com.google.common.base.Objects;
import org.hamcrest.Description;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Convenience abstract implementation of {@link Expression} which has an empty
 * implementation of {@link #describeTo(Description)}.
 *
 * @since 2/27/12
 */
public abstract class AbstractExpression<T> implements Expression<T> {
  @Nullable
  private final String simpleDescription;

  protected AbstractExpression() {
    this(null);
  }

  protected AbstractExpression(@Nullable final String simpleDescription) {
    this.simpleDescription = simpleDescription;
  }

  /**
   * <p>
   * By default adds nothing to the description of this expression. For debugging purpose your
   * implementation could add descriptions of what is done during {@link #get()}, for example
   * {@code "getting name of document"}.
   * </p>
   * <p>
   * For convenience, if you have provided a simple description via the constructor, this will
   * be added to the description.
   * </p>
   *
   * @param description description to enrich
   */
  @Override
  public void describeTo(@Nonnull final Description description) {
    if (simpleDescription != null) {
      description.appendText(simpleDescription);
    }
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("simpleDescription", simpleDescription)
            .toString();
  }
}

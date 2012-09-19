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

package net.joala.condition.timing;

import com.google.common.base.Objects;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * A self describing object. Adds a given string as plain text to a provided description object.
 * </p>
 *
 * @since 9/14/12
 */
public abstract class SimpleSelfDescribing implements SelfDescribing {
  /**
   * The simple description.
   */
  @Nullable
  protected final String simpleDescription;

  /**
   * <p>
   * Constructor with a simple description.
   * </p>
   *
   * @param simpleDescription description to add; {@code null} if this object shall not provide any description
   */
  protected SimpleSelfDescribing(@Nullable final String simpleDescription) {
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

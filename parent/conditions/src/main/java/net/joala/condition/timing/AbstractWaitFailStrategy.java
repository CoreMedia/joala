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

package net.joala.condition.timing;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import net.joala.time.TimeFormat;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;

import java.util.concurrent.TimeUnit;

import static net.joala.condition.util.DescriptionUtil.describeTo;

/**
 * <p>
 * Abstract implementation of {@link WaitFailStrategy}. Provides some commonly used
 * methods for fail strategy implementations.
 * </p>
 *
 * @since 8/27/12
 */
public abstract class AbstractWaitFailStrategy implements WaitFailStrategy {

  /**
   * <p>
   * Enhances the description for a failure message by the evaluated function and how long it took to
   * meet the timeout.
   * </p>
   *
   * @param message        original (plain) message
   * @param function       function evaluated
   * @param input          the input to the function
   * @param consumedMillis how long it took until timeout  @return enhanced message
   * @return enhanced description
   */
  @NonNull
  protected String addTimeoutDescription(@Nullable final String message,
                                         @NonNull final Object function,
                                         @NonNull final Object input,
                                         final long consumedMillis) {
    final Description description = new StringDescription();
    description.appendText(message == null ? "Failed to evaluate." : message);
    description.appendText(" - after ");
    description.appendText(TimeFormat.format(consumedMillis, TimeUnit.MILLISECONDS));
    description.appendText(" evaluating ");
    describeTo(description, function);
    description.appendText(" on ");
    describeTo(description, input);
    return description.toString();
  }

}

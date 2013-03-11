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

package net.joala.condition;

import net.joala.condition.timing.AbstractWaitFailStrategy;
import org.hamcrest.Matcher;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * <p>
 * Strategy to fail with an assertion error if the condition is not fulfilled.
 * </p>
 *
 * @since 8/23/12
 */
public class WaitAssertionFailStrategy extends AbstractWaitFailStrategy {
  @Override
  public void fail(@Nullable final String reason,
                   @Nonnull final Object function,
                   @Nonnull final Object input,
                   @Nonnull final Throwable exception,
                   @Nonnegative final long consumedMillis) {
    assertThat(
            addTimeoutDescription(reason, function, input, consumedMillis),
            exception,
            new WaitFailNoExceptionMatcher(function));
  }

  @Override
  public <T> void fail(@Nullable final String reason,
                       @Nonnull final Object function,
                       @Nonnull final Object input,
                       @Nullable final T lastValue,
                       @Nonnull final Matcher<? super T> matcher,
                       @Nonnegative final long consumedMillis) {
    assertThat(addTimeoutDescription(reason, function, input, consumedMillis), lastValue, matcher);
  }
}

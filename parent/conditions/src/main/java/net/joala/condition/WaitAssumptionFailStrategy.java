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

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import net.joala.condition.timing.AbstractWaitFailStrategy;
import net.joala.condition.util.EnhanceDescriptionBy;
import org.hamcrest.Matcher;

import static org.junit.Assume.assumeThat;

/**
 * <p>
 * Strategy to fail with an assumption violation if the condition is not fulfilled.
 * </p>
 *
 * @since 8/23/12
 */
public class WaitAssumptionFailStrategy extends AbstractWaitFailStrategy {
  @Override
  public void fail(@Nullable final String reason,
                   @NonNull final Object function,
                   @NonNull final Object input,
                   @NonNull final Throwable exception,
                   final long consumedMillis) {
    // enhanceDescriptionBy: Workaround, see https://github.com/KentBeck/junit/pull/489
    assumeThat(
            exception,
            EnhanceDescriptionBy.enhanceDescriptionBy(
                    addTimeoutDescription(reason, function, input, consumedMillis),
                    new WaitFailNoExceptionMatcher(function)));
  }

  @Override
  public <T> void fail(@Nullable final String reason,
                       @NonNull final Object function,
                       @NonNull final Object input,
                       @Nullable final T lastValue,
                       @NonNull final Matcher<? super T> matcher,
                       final long consumedMillis) {
    // enhanceDescriptionBy: Workaround, see https://github.com/KentBeck/junit/pull/489
    assumeThat(
            lastValue,
            EnhanceDescriptionBy.enhanceDescriptionBy(addTimeoutDescription(reason, function, input, consumedMillis), matcher));
  }
}

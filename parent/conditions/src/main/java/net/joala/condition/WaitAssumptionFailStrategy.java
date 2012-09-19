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

import net.joala.condition.timing.AbstractWaitFailStrategy;
import net.joala.matcher.decorator.EnhanceDescriptionBy;
import org.hamcrest.Matcher;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

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
  public void fail(@Nonnull final String reason,
                   @Nonnull final Object function,
                   @Nonnull final Object input,
                   @Nonnull final Throwable exception,
                   @Nonnegative final long consumedMillis) {
    // enhanceDescriptionBy: Workaround, see https://github.com/KentBeck/junit/pull/489
    assumeThat(
            exception,
            EnhanceDescriptionBy.enhanceDescriptionBy(
                    addTimeoutDescription(reason, function, input, consumedMillis),
                    new WaitFailNoExceptionMatcher(function)));
  }

  @Override
  public <T> void fail(@Nonnull final String reason,
                       @Nonnull final Object function,
                       @Nonnull final Object input,
                       @Nonnull final T lastValue,
                       @Nonnull final Matcher<? super T> matcher,
                       @Nonnegative final long consumedMillis) {
    // enhanceDescriptionBy: Workaround, see https://github.com/KentBeck/junit/pull/489
    assumeThat(
            lastValue,
            EnhanceDescriptionBy.enhanceDescriptionBy(addTimeoutDescription(reason, function, input, consumedMillis), matcher));
  }
}

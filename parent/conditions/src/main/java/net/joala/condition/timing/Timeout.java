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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * A timeout which can be converted to different time-units.
 * </p>
 *
 * @since 8/22/12
 * @deprecated since 0.5.0; use {@link net.joala.time.Timeout} instead
 */
@Deprecated
public interface Timeout {
  /**
   * Get the timeout in the given unit.
   *
   * @param targetUnit the timeunit to use
   * @return timeout in the given unit
   */
  @Nonnegative
  @SuppressWarnings("PMD.ShortMethodName")
  long in(@Nonnull TimeUnit targetUnit);

  /**
   * <p>
   * Get the timeout in the given unit adjusted by the given factor.
   * </p>
   *
   * @param targetUnit the timeunit to use
   * @param factor     factor to adjust the timeout
   * @return timeout adjust by factor
   */
  @Nonnegative
  @SuppressWarnings("PMD.ShortMethodName")
  long in(@Nonnull TimeUnit targetUnit, @Nonnegative double factor);
}

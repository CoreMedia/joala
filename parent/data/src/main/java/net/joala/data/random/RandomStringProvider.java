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

package net.joala.data.random;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @since 9/17/12
 */
public interface RandomStringProvider extends RandomDataProvider<String> {
  @Nonnull
  RandomStringProvider length(@Nonnegative int len);

  @Nonnull
  RandomStringProvider maxLength(@Nonnegative int len);

  @Nonnull
  RandomStringProvider minLength(@Nonnegative int len);

  @Nonnull
  RandomStringProvider type(@Nonnull RandomStringType type);

  @Nonnull
  RandomStringProvider prefix(@Nullable String prefix);

  @Nonnull
  RandomStringProvider postfix(@Nullable String postfix);
}

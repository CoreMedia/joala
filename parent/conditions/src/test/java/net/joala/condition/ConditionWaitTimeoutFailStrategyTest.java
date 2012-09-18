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

import net.joala.base.ConditionWaitTimeoutFailStrategy;

/**
 * @since 8/27/12
 */
public class ConditionWaitTimeoutFailStrategyTest extends ConditionWaitFailStrategyTest<ConditionWaitTimeoutFailStrategy, ConditionTimeoutException> {
  @Override
  protected Class<ConditionTimeoutException> getRaisedExceptionType() {
    return ConditionTimeoutException.class;
  }

  @Override
  protected ConditionWaitTimeoutFailStrategy getFailStrategy() {
    return new ConditionWaitTimeoutFailStrategy();
  }
}

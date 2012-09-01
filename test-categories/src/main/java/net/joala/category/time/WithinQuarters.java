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

package net.joala.category.time;

/**
 * <p>
 * Tests which promise to be done in 15 minutes to an hour. Take it as <em>slow</em>.
 * </p>
 * <p>
 *   <strong>Recommendation:</strong> Take it for tests from about 15 to 45 minutes.
 * </p>
 *
 * @since 8/30/12
 */
public interface WithinQuarters extends WithinMinutes {
}

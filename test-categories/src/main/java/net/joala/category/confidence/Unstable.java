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

package net.joala.category.confidence;

/**
 * <p>
 * Marks a test as being unstable. You might want to run exclude unstable tests from regular test jobs in order
 * to prevent the broken window effect not being able to see that new test failures occurred. Mind that you should
 * still set up some job executing the unstable tests.
 * </p>
 *
 * @since 8/30/12
 * @see Stable
 */
public interface Unstable {
}

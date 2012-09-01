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

package net.joala.category;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a test class or test method as belonging to one or more categories of tests.
 * The value is an array of arbitrary classes.
 * <p/>
 * This annotation is only interpreted by the Categories runner (at present).
 * <p/>
 * For example:
 * <pre>
 * public interface FastTests {}
 * public interface SlowTests {}
 *
 * public static class A {
 * &#064;Test
 * public void a() {
 * fail();
 * }
 *
 * &#064;Category(SlowTests.class)
 * &#064;Test
 * public void b() {
 * }
 * }
 *
 * &#064;Category({SlowTests.class, FastTests.class})
 * public static class B {
 * &#064;Test
 * public void c() {
 *
 * }
 * }
 * </pre>
 * <p/>
 * For more usage, see code example on {@link org.junit.experimental.categories.Categories}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.PACKAGE, ElementType.TYPE})
public @interface Category {
  Class<?>[] value();
}
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

package net.joala.matcher.reflect;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Modifier;

/**
 * @since 10/9/12
 */
public class ClassHasModifier<T extends Class<?>> extends TypeSafeMatcher<T> {
  private final int modifierFlag;

  public ClassHasModifier(final int modifierFlag) {
    this.modifierFlag = modifierFlag;
  }

  @Override
  protected boolean matchesSafely(final T item) {
    return (item.getModifiers() & modifierFlag) != 0;
  }

  @Override
  public void describeTo(final Description description) {
    description
            .appendText("is ")
            .appendText(Modifier.toString(modifierFlag));
  }

  @Override
  protected void describeMismatchSafely(final T item, final Description description) {
    description
            .appendText("but was ")
            .appendText(Modifier.toString(item.getModifiers()));
  }

  public static <T extends Class<?>> Matcher<T> classIsAbstract() {
    return new ClassHasModifier<T>(Modifier.ABSTRACT);
  }

  public static <T extends Class<?>> Matcher<T> classIsFinal() {
    return new ClassHasModifier<T>(Modifier.FINAL);
  }

  public static <T extends Class<?>> Matcher<T> classIsInterface() {
    return new ClassHasModifier<T>(Modifier.INTERFACE);
  }

  public static <T extends Class<?>> Matcher<T> classIsPrivate() {
    return new ClassHasModifier<T>(Modifier.PRIVATE);
  }

  public static <T extends Class<?>> Matcher<T> classIsProtected() {
    return new ClassHasModifier<T>(Modifier.PROTECTED);
  }

  public static <T extends Class<?>> Matcher<T> classIsPublic() {
    return new ClassHasModifier<T>(Modifier.PUBLIC);
  }

  public static <T extends Class<?>> Matcher<T> classIsStatic() {
    return new ClassHasModifier<T>(Modifier.STATIC);
  }

  public static <T extends Class<?>> Matcher<T> classIsStrict() {
    return new ClassHasModifier<T>(Modifier.STRICT);
  }
}

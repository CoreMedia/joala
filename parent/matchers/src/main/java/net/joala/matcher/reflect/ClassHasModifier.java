package net.joala.matcher.reflect;

import org.hamcrest.Description;
import org.hamcrest.Factory;
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

  @Factory
  public static <T extends Class<?>> Matcher<T> classIsAbstract() {
    return new ClassHasModifier<T>(Modifier.ABSTRACT);
  }

  @Factory
  public static <T extends Class<?>> Matcher<T> classIsFinal() {
    return new ClassHasModifier<T>(Modifier.FINAL);
  }

  @Factory
  public static <T extends Class<?>> Matcher<T> classIsInterface() {
    return new ClassHasModifier<T>(Modifier.INTERFACE);
  }

  @Factory
  public static <T extends Class<?>> Matcher<T> classIsPrivate() {
    return new ClassHasModifier<T>(Modifier.PRIVATE);
  }

  @Factory
  public static <T extends Class<?>> Matcher<T> classIsProtected() {
    return new ClassHasModifier<T>(Modifier.PROTECTED);
  }

  @Factory
  public static <T extends Class<?>> Matcher<T> classIsPublic() {
    return new ClassHasModifier<T>(Modifier.PUBLIC);
  }

  @Factory
  public static <T extends Class<?>> Matcher<T> classIsStatic() {
    return new ClassHasModifier<T>(Modifier.STATIC);
  }

  @Factory
  public static <T extends Class<?>> Matcher<T> classIsStrict() {
    return new ClassHasModifier<T>(Modifier.STRICT);
  }
}

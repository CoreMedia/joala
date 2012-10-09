package net.joala.matcher.reflect;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @since 10/9/12
 */
public class MemberHasModifier<T extends Member> extends TypeSafeMatcher<T> {
  private final int modifierFlag;

  public MemberHasModifier(final int modifierFlag) {
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
  public static <T extends Method> Matcher<T> memberIsAbstract() {
    return new MemberHasModifier<T>(Modifier.ABSTRACT);
  }

  @Factory
  public static <T extends Member> Matcher<T> memberIsFinal() {
    return new MemberHasModifier<T>(Modifier.FINAL);
  }

  @Factory
  public static <T extends Method> Matcher<T> memberIsNative() {
    return new MemberHasModifier<T>(Modifier.NATIVE);
  }

  @Factory
  public static <T extends Member> Matcher<T> memberIsPrivate() {
    return new MemberHasModifier<T>(Modifier.PRIVATE);
  }

  @Factory
  public static <T extends Member> Matcher<T> memberIsProtected() {
    return new MemberHasModifier<T>(Modifier.PROTECTED);
  }

  @Factory
  public static <T extends Member> Matcher<T> memberIsPublic() {
    return new MemberHasModifier<T>(Modifier.PUBLIC);
  }

  @Factory
  public static <T extends Member> Matcher<T> memberIsStatic() {
    return new MemberHasModifier<T>(Modifier.STATIC);
  }

  @Factory
  public static <T extends Method> Matcher<T> memberIsStrict() {
    return new MemberHasModifier<T>(Modifier.STRICT);
  }

  @Factory
  public static <T extends Method> Matcher<T> memberIsSynchronized() {
    return new MemberHasModifier<T>(Modifier.SYNCHRONIZED);
  }

  @Factory
  public static <T extends Field> Matcher<T> memberIsVolatile() {
    return new MemberHasModifier<T>(Modifier.VOLATILE);
  }
}

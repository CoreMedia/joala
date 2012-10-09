package net.joala.matcher.reflect;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.AccessibleObject;

/**
 * @since 10/9/12
 */
public class IsAccessible<T extends AccessibleObject> extends CustomTypeSafeMatcher<T> {
  public IsAccessible() {
    super("is accessible");
  }

  @Override
  protected boolean matchesSafely(final T item) {
    return item.isAccessible();
  }

  @Factory
  public static <T extends AccessibleObject> Matcher<T> isAccessible() {
    return new IsAccessible<T>();
  }
}

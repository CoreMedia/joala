package net.joala.matcher.reflect;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import java.lang.reflect.AccessibleObject;

/**
 * <p>
 * Verifies if a given object is accessible (a field, method or such).
 * </p>
 *
 * @param <T> accessible object type
 * @since 10/9/12
 */
public class IsAccessible<T extends AccessibleObject> extends CustomTypeSafeMatcher<T> {
  /**
   * Constructor.
   */
  public IsAccessible() {
    super("is accessible");
  }

  @Override
  protected boolean matchesSafely(final T item) {
    return item.isAccessible();
  }

  /**
   * Create matcher which verifies if the given object is accessible.
   *
   * @param <T> accessible object type
   * @return matcher
   */
  @Factory
  public static <T extends AccessibleObject> Matcher<T> isAccessible() {
    return new IsAccessible<T>();
  }
}

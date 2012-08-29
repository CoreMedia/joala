package net.joala.matcher.exception;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;

import static org.hamcrest.core.IsEqual.equalTo;

/**
 * <p>
 * Matcher to search stacktrace of exception if it get caused by a given exception somewhere.
 * </p>
 *
 * @since 8/28/12
 */
public class CausedBy extends CustomTypeSafeMatcher<Throwable> {
  private final Throwable cause;

  public CausedBy(@Nonnull final Throwable cause) {
    super("is caused by");
    this.cause = cause;
  }

  @Override
  protected boolean matchesSafely(final Throwable item) {
    return item.getCause() != null && (item.getCause() == cause || matchesSafely(item.getCause()));
  }

  @Factory
  public static Matcher<Throwable> causedBy(final Throwable expected) {
    return new CausedBy(expected);
  }

}

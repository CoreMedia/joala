package net.joala.matcher.decorator;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.core.DescribedAs;

/**
 * <p>
 * Use this wrapper for matchers to enhance it by additional
 * description. Can be used for example for assumption failures
 * where with current API it is not possible to add a message.
 * </p>
 *
 * @see <a href="https://github.com/KentBeck/junit/pull/489">Improve Assume to allow custom message</a>
 * @since 8/28/12
 */
public class EnhanceDescriptionBy<T> extends DescribedAs<T> {

  private final Matcher<T> matcher;

  public EnhanceDescriptionBy(final String descriptionTemplate, final Matcher<T> matcher, final Object... values) {
    super(descriptionTemplate, matcher, values);
    this.matcher = matcher;
  }

  @Override
  public void describeTo(final Description description) {
    matcher.describeTo(description);
    description.appendText(" (");
    super.describeTo(description);
    description.appendText(")");
  }

  @Factory
  public static <T> Matcher<T> enhanceDescriptionBy(final String descriptionTemplate, final Matcher<T> matcher, final Object... values) {
    return new EnhanceDescriptionBy<T>(descriptionTemplate, matcher, values);
  }

}

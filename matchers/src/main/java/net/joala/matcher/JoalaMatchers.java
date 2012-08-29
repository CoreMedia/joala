package net.joala.matcher;

import net.joala.matcher.decorator.EnhanceDescriptionBy;
import net.joala.matcher.exception.CausedBy;
import net.joala.matcher.exception.MessageContains;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;

/**
 * @since 8/28/12
 */
public final class JoalaMatchers {
  private JoalaMatchers() {
  }

  public static Matcher<Throwable> causedBy(final Throwable expected) {
    return CausedBy.causedBy(expected);
  }

  public static Matcher<Throwable> messageContains(final String expected) {
    return MessageContains.messageContains(expected);
  }

  public static Matcher<Throwable> messageContains(final String expected, final boolean recurseCauses) {
    return MessageContains.messageContains(expected, recurseCauses);
  }

  public static <T> Matcher<T> enhanceDescriptionBy(final String descriptionTemplate, final Matcher<T> matcher, final Object... values) {
    return EnhanceDescriptionBy.enhanceDescriptionBy(descriptionTemplate, matcher, values);
  }
}

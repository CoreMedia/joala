package net.joala.condition;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

/**
 * @since 8/27/12
 */
abstract class AbstractConditionWaitFailStrategy implements ConditionWaitFailStrategy {
  @Nonnull
  protected String addTimeoutDescription(@Nullable final String message, @Nonnull final SelfDescribing function, @Nonnegative final long consumedMillis) {
    final Description description = new StringDescription();
    description.appendText(message != null ? message : "Failed to evaluate.");
    description.appendText(" - after ");
    description.appendText(formatMillis(consumedMillis));
    description.appendText(" evaluating function ");
    description.appendDescriptionOf(function);
    return description.toString();
  }

  @Nonnull
  private String formatMillis(@Nonnegative final long millis) {
    final long amount;
    final String unit;
    if (TimeUnit.MILLISECONDS.toSeconds(millis) < 2) {
      amount = millis;
      unit = "ms";
    } else if (TimeUnit.MILLISECONDS.toMinutes(millis) < 5) {
      amount = TimeUnit.MILLISECONDS.toSeconds(millis);
      unit = "s";
    } else if (TimeUnit.MILLISECONDS.toHours(millis) < 2) {
      amount = TimeUnit.MILLISECONDS.toMinutes(millis);
      unit = "min";
    } else {
      amount = TimeUnit.MILLISECONDS.toHours(millis);
      unit = "h";
    }
    return String.format("%d %s", amount, unit);
  }

}

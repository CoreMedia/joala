package net.joala.condition;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @since 8/27/12
 */
final class ConditionWaitFailNoExceptionMatcher extends CustomTypeSafeMatcher<ExpressionEvaluationException> {
  private final ConditionFunction<?> function;

  ConditionWaitFailNoExceptionMatcher(final ConditionFunction<?> function) {
    super("evaluation without exception");
    this.function = function;
  }

  @Override
  protected boolean matchesSafely(final ExpressionEvaluationException item) {
    return item == null;
  }

  @Override
  protected void describeMismatchSafely(final ExpressionEvaluationException item, final Description mismatchDescription) {
    final StringWriter out = new StringWriter();
    item.printStackTrace(new PrintWriter(out));
    mismatchDescription.appendText("failed to evaluate function:");
    mismatchDescription.appendDescriptionOf(function);
    mismatchDescription.appendText("because of:");
    mismatchDescription.appendText(out.toString());
  }
}

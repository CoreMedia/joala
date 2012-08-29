package net.joala.condition;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Condition Function which also caches the value retrieved.
 *
 * @since 2/24/12
 */
class ConditionFunction<T> implements Function<Matcher<? super T>, Boolean>, SelfDescribing {
  @Nullable
  private T cached;
  @Nonnull
  private final Expression<T> expression;

  /**
   * Expression to evaluate.
   *
   * @param expression expression
   */
  ConditionFunction(@Nonnull final Expression<T> expression) {
    this.expression = expression;
  }

  /**
   * Apply the matcher on the given value retrieved via expression.
   *
   * @param matcher matcher to use; null to match always
   * @return if the value matched or not
   * @see #getCached() last retrieved value
   */
  @Override
  @Nonnull
  public Boolean apply(@Nullable final Matcher<? super T> matcher) {
    cached = expression.get();
    return matcher == null || matcher.matches(cached);
  }

  /**
   * Contains the last retrieved value.
   *
   * @return last retrieved value
   */
  @Nullable
  public T getCached() {
    return cached;
  }

  /**
   * Adds a description useful for debugging.
   *
   * @param description description to enrich
   */
  @Override
  public void describeTo(@Nonnull final Description description) {
    description.appendDescriptionOf(expression);
  }


  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("expression", expression)
            .add("cached", cached)
            .toString();
  }


}

/*
 * Copyright 2012 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

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

package net.joala.bdd.watcher;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * <p>
 * The {@link org.junit.rules.TestWatcher} extended by reporting of skipped tests because of an
 * {@link org.junit.internal.AssumptionViolatedException}.
 * </p>
 * <p>
 * This is actually caused by the change for <a href="https://github.com/KentBeck/junit/issues/296">issue 296</a> which caused the
 * {@link org.junit.internal.AssumptionViolatedException} to be completely ignored.
 * </p>
 *
 * @since 6/25/12
 */
public class ExtendedTestWatcher extends TestWatcher {
  @Override
  public Statement apply(final Statement base, final Description description) {
    // Copy from TestWatcher in junit v4.10. We need to add the skipped method.
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        starting(description);
        try {
          base.evaluate();
          succeeded(description);
        } catch (AssumptionViolatedException e) {
          skipped(e, description);
          throw e;
        } catch (Throwable t) {
          failed(t, description);
          throw t;
        } finally {
          finished(description);
        }
      }
    };
  }

  /**
   * Invoked when a test is skipped because of an {@link org.junit.internal.AssumptionViolatedException}.
   *
   * @param e           the exception
   * @param description the description
   */
  protected void skipped(final AssumptionViolatedException e, final Description description) {
    // Empty, override if you like
  }

}

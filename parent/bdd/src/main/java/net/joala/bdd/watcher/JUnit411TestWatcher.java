/*
 * Copyright 2012 CoreMedia AG
 *
 * This file is part of Joala.
 *
 * Joala is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Joala is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

// Copy of org.junit.rules.TestWatcher from not yet released 4.11 JUnit Release
// TODO: Remove as soon as updated to JUnit 4.11
package net.joala.bdd.watcher;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TestWatcher is a base class for Rules that take note of the testing
 * action, without modifying it. For example, this class will keep a log of each
 * passing and failing test:
 * <p/>
 * <pre>
 * public static class WatchmanTest {
 * 	private static String watchedLog;
 *
 * 	&#064;Rule
 * 	public TestWatcher watchman= new TestWatcher() {
 * 		&#064;Override
 * 		protected void failed(Throwable e, Description description) {
 * 			watchedLog+= description + &quot;\n&quot;;
 *                 }
 *
 * 		&#064;Override
 * 		protected void succeeded(Description description) {
 * 			watchedLog+= description + &quot; &quot; + &quot;success!\n&quot;;
 *                 }
 *         };
 *
 * 	&#064;Test
 * 	public void fails() {
 * 		fail();
 *         }
 *
 * 	&#064;Test
 * 	public void succeeds() {
 *         }
 * }
 * </pre>
 *
 * @since 4.9
 */
public abstract class JUnit411TestWatcher extends TestWatcher {
  @Override
  public Statement apply(final Statement base, final Description description) {
    return new ReportingStatement(description, base);
  }

  /**
   * Invoked when a test succeeds
   *
   * @param description description of the current test-case
   */
  @Override
  protected void succeeded(final Description description) {
  }

  /**
   * Invoked when a test fails
   *
   * @param e causing exception
   * @param description description of the current test-case
   */
  @Override
  protected void failed(final Throwable e, final Description description) {
  }

  /**
   * Invoked when a test is skipped due to a failed assumption.
   *
   * @param e causing exception
   * @param description description of the current test-case
   */
  protected void skipped(final AssumptionViolatedException e, final Description description) {
  }

  /**
   * Invoked when a test is about to start
   *
   * @param description description of the current test-case
   */
  @Override
  protected void starting(final Description description) {
  }

  /**
   * Invoked when a test method finishes (whether passing or failing)
   *
   * @param description description of the current test-case
   */
  @Override
  protected void finished(final Description description) {
  }

  private final class ReportingStatement extends Statement {
    private final Description description;
    private final Statement base;

    private ReportingStatement(final Description description, final Statement base) {
      this.description = description;
      this.base = base;
    }

    @Override
    public void evaluate() throws Throwable { // NOSONAR: Copied from original class
      final List<Throwable> errors = new ArrayList<Throwable>();

      startingQuietly(description, errors);
      try {
        base.evaluate();
        succeededQuietly(description, errors);
      } catch (AssumptionViolatedException e) {
        errors.add(e);
        skippedQuietly(e, description, errors);
      } catch (Throwable t) { // NOSONAR: Copied from original class
        errors.add(t);
        failedQuietly(t, description, errors);
      } finally {
        finishedQuietly(description, errors);
      }

      MultipleFailureException.assertEmpty(errors);
    }

    private void succeededQuietly(final Description description,
                                  final Collection<Throwable> errors) {
      try {
        succeeded(description);
      } catch (Throwable t) { // NOSONAR: Copied from original class
        errors.add(t);
      }
    }

    private void failedQuietly(final Throwable t, final Description description,
                               final Collection<Throwable> errors) {
      try {
        failed(t, description);
      } catch (Throwable t1) { // NOSONAR: Copied from original class
        errors.add(t1);
      }
    }

    private void skippedQuietly(final AssumptionViolatedException t, final Description description,
                                final Collection<Throwable> errors) {
      try {
        skipped(t, description);
      } catch (Throwable t1) { // NOSONAR: Copied from original class
        errors.add(t1);
      }
    }

    private void startingQuietly(final Description description,
                                 final Collection<Throwable> errors) {
      try {
        starting(description);
      } catch (Throwable t) { // NOSONAR: Copied from original class
        errors.add(t);
      }
    }

    private void finishedQuietly(final Description description,
                                 final Collection<Throwable> errors) {
      try {
        finished(description);
      } catch (Throwable t) { // NOSONAR: Copied from original class
        errors.add(t);
      }
    }

  }
}

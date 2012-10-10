package net.joala.testlet;

import com.google.common.base.Objects;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runners.model.MultipleFailureException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *   Listener while running the testlets which will collect any failures.
 * </p>
 * @since 10/4/12
 */
class ExceptionCollectingRunListener extends RunListener {
  /**
   * Assumption on number of test failures.
   */
  private static final int INITIALLY_EXPECTED_FAILURE_COUNT = 5;
  /**
   * List of collected exceptions.
   */
  private final List<Throwable> exceptions = new ArrayList<Throwable>(INITIALLY_EXPECTED_FAILURE_COUNT);

  @Override
  public void testFailure(final Failure failure) {
    exceptions.add(failure.getException());
  }

  @Override
  public void testAssumptionFailure(final Failure failure) {
    exceptions.add(failure.getException());
  }

  /**
   * <p>
   * Asserts that no exceptions occurred. See {@link MultipleFailureException#assertEmpty(List)}
   * for details.
   * </p>
   *
   * @throws Throwable raised by {@link MultipleFailureException#assertEmpty(List)} if any errors got reported
   */
  @SuppressWarnings("ProhibitedExceptionDeclared")
  public void assertNoFailures() throws Throwable { // NOSONAR: exception Throwable inherited from JUnit API
    MultipleFailureException.assertEmpty(exceptions);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("exceptions", exceptions)
            .toString();
  }
}

package net.joala.lab.junit.template;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runners.model.MultipleFailureException;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 10/4/12
 */
class ExceptionCollectingRunListener extends RunListener {
  private static final int INITIALLY_EXPECTED_FAILURE_COUNT = 5;
  private final List<Throwable> exceptions = new ArrayList<Throwable>(INITIALLY_EXPECTED_FAILURE_COUNT);

  @Override
  public void testFailure(final Failure failure) throws Exception {
    exceptions.add(failure.getException());
  }

  @Override
  public void testAssumptionFailure(final Failure failure) {
    exceptions.add(failure.getException());
  }

  public void assertNoFailures() throws Throwable {
    MultipleFailureException.assertEmpty(exceptions);
  }
}

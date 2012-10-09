package net.joala.testlet;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * Abstract implementation which handles starting the tests. Implementing methods just need
 * to add the test methods annotated with {@link Test}. The artifact under test can be retrieved
 * via {@link #getTestling()}.
 * </p>
 * <p>
 * In order to prevent execution of tests within IDE which will recognize the testlets as
 * <em>false-positive</em> standalone tests just add the annotation {@link Ignore}.
 * The testlet runner will still be able to execute it.
 * </p>
 *
 * @since 10/9/12
 */
public abstract class AbstractTestlet<T> implements Testlet {
  /**
   * The artifact to test.
   */
  @Nonnull
  private final T testling;

  /**
   * <p>
   * Builds the testlet with the specified artifact under test.
   * </p>
   *
   * @param testling artifact under test
   */
  protected AbstractTestlet(@Nonnull final T testling) {
    checkNotNull(testling, "Testling must not be null.");
    this.testling = testling;
  }

  /**
   * <p>
   * Retrieve the artifact under test.
   * </p>
   *
   * @return the artifact under test
   */
  @Nonnull
  protected T getTestling() {
    return testling;
  }

  @SuppressWarnings("ProhibitedExceptionDeclared")
  @Override
  public final void run() throws Throwable { // NOSONAR: Throwable exception inherited from JUnit
    final BlockJUnit4TestletRunner runner = new BlockJUnit4TestletRunner(this);
    final JUnitCore jUnitCore = new JUnitCore();
    final ExceptionCollectingRunListener listener = new ExceptionCollectingRunListener();
    jUnitCore.addListener(listener);
    jUnitCore.run(runner);
    listener.assertNoFailures();
  }
}

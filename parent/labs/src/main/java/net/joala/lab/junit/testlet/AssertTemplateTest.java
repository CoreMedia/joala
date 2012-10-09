package net.joala.lab.junit.testlet;

import org.junit.runner.JUnitCore;

import javax.annotation.Nonnull;

/**
 * @since 10/4/12
 */
final class AssertTemplateTest {
  private AssertTemplateTest() {
  }

  @SuppressWarnings("ProhibitedExceptionDeclared")
  static void assertNoFailures(@Nonnull final Object testClassInstance) throws Throwable {  // NOSONAR: Throwable comes from JUnit API
    final TestClassInstanceRunner runner = new TestClassInstanceRunner(testClassInstance);
    final JUnitCore jUnitCore = new JUnitCore();
    final ExceptionCollectingRunListener listener = new ExceptionCollectingRunListener();
    jUnitCore.addListener(listener);
    jUnitCore.run(runner);
    listener.assertNoFailures();
  }

}

package net.joala.lab.junit.testlet;

/**
 * <p>
 *   A testlet is a set of tests to run. Typically one set of tests is responsible for one
 *   small feature you want to test, like if the {@code toString()} method meets your requirements and such.
 * </p>
 * @since 10/9/12
 */
public interface Testlet {
  /**
   * <p>
   * Runs the testlet. This trigger a group of tests with the provided artifact(s).
   * </p>
   * @throws Throwable in case of an error; most likely a single assertion error or multiple failures
   * bundled in one exception; all of them recognized by standard JUnit executors.
   */
  @SuppressWarnings("ProhibitedExceptionDeclared")
  void run() throws Throwable; // NOSONAR: exception Throwable inherited from JUnit API
}

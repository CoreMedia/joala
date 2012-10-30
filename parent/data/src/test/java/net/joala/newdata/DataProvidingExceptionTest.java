package net.joala.newdata;

import org.junit.Test;

import static net.joala.testlet.ExceptionClassTestlet.exceptionClassTestlet;

/**
 * <p>
 * Tests {@link DataProvidingException}.
 * </p>
 *
 * @since 10/9/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
public class DataProvidingExceptionTest {
  @Test
  public void verify_exception_class_requirements() throws Throwable {
    exceptionClassTestlet(DataProvidingException.class).run();
  }
}

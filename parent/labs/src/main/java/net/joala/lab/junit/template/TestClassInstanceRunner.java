package net.joala.lab.junit.template;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.List;

/**
* @since 10/4/12
*/
class TestClassInstanceRunner extends BlockJUnit4ClassRunner {
  private final Object testClassInstance;

  TestClassInstanceRunner(@Nonnull final Object testClassInstance) throws InitializationError {
    super(testClassInstance.getClass());
    this.testClassInstance = testClassInstance;
  }

  @Override
  protected Object createTest() throws Exception {
    return testClassInstance;
  }

  @Override
  protected void validateConstructor(final List<Throwable> errors) {
    // don't validate
  }

  @Override
  protected Statement classBlock(final RunNotifier notifier) {
    return childrenInvoker(notifier);
  }

  @Override
  protected Annotation[] getRunnerAnnotations() {
    return new Annotation[0];
  }
}

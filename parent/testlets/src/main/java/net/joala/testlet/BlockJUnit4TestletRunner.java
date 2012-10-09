package net.joala.testlet;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.junit.Ignore;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * <p>
 * Runner which gets instantiated by {@link AbstractTestlet} in order to run the testlet.
 * </p>
 *
 * @since 10/4/12
 */
final class BlockJUnit4TestletRunner extends BlockJUnit4ClassRunner {
  private final Object testlet;

  /**
   * <p>
   * Set the testlet to run.
   * </p>
   *
   * @param testlet testlet to run
   * @throws InitializationError forwarded from {@link BlockJUnit4ClassRunner#BlockJUnit4ClassRunner(Class)}
   */
  BlockJUnit4TestletRunner(@Nonnull final Object testlet) throws InitializationError {
    super(Preconditions.checkNotNull(testlet).getClass());
    this.testlet = testlet;
  }

  /**
   * <p>
   * Actually instead of creating the test just returns the prepared testlet.
   * </p>
   *
   * @return testlet
   */
  @Override
  protected Object createTest() {
    return testlet;
  }

  /**
   * Validation of constructor disabled.
   *
   * @param errors unused
   */
  @Override
  protected void validateConstructor(final List<Throwable> errors) {
    // don't validate
  }

  /**
   * <p>
   * Any class level annotations like {@link Ignore} are ignored.
   * </p>
   *
   * @return empty list
   */
  @Override
  protected Annotation[] getRunnerAnnotations() {
    return new Annotation[0];
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("testlet", testlet)
            .toString();
  }
}

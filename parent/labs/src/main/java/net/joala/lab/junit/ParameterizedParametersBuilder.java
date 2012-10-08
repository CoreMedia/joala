package net.joala.lab.junit;

import java.util.Collection;

/**
 * <p>
 * Builder for parameters for running parametrized JUnit tests.
 * </p>
 *
 * @since 10/8/12
 */
public interface ParameterizedParametersBuilder {
  /**
   * <p>
   * Add objects for the one test run.
   * </p>
   *
   * @param objects objects to add, each must match to one constructor argument
   * @return self-reference
   */
  ParameterizedParametersBuilder add(Object... objects);

  /**
   * Build the data which then should be returned by the methood annotated
   * with {@code &#64;Parameterized.Parameters}.
   *
   * @return parametrized test data
   */
  Collection<Object[]> build();
}

package net.joala.lab.junit;

import java.util.List;

/**
 * <p>
 * Builder for parameters assuming that all parameters are singletons. Thus each single
 * object added will cause a test-run.
 * </p>
 *
 * @since 10/8/12
 */
public class SingletonParameterizedParametersBuilder extends DefaultParameterizedParametersBuilder {
  public SingletonParameterizedParametersBuilder(final Class<?> testClass) {
    super(testClass);
  }

  @Override
  public ParameterizedParametersBuilder add(final Object... objects) {
    for (final Object object : objects) {
      super.add(object);
    }
    return this;
  }
}

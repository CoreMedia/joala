package net.joala.lab.junit;

/**
 * @since 10/8/12
 */
public final class ParameterizedParametersBuilders {

  private ParameterizedParametersBuilders() {
  }

  public static ParameterizedParametersBuilder defaultParametersBuilder(final Class<?> testClass) {
    return new DefaultParameterizedParametersBuilder(testClass);
  }

  public static ParameterizedParametersBuilder singletonParametersBuilder(final Class<?> testClass) {
    return new SingletonParameterizedParametersBuilder(testClass);
  }
}

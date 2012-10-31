package net.joala.newdata.random;

/**
 * In order to prevent cyclic dependencies to joala-data we have to use our
 * own reference here.
 *
 * @since 10/31/12
 */
final class PoorMansReference<T> {
  private T value;

  static <T> PoorMansReference<T> ref() {
    return new PoorMansReference<T>();
  }

  public T get() {
    return value;
  }

  public void set(final T value) {
    this.value = value;
  }
}

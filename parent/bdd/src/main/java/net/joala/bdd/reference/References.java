package net.joala.bdd.reference;

import javax.annotation.Nonnull;

/**
 * <p>
 * Factory methods for creating references. These factory methods are meant to provide
 * a shortcut to create new instances.
 * </p>
 *
 * @since 9/27/12
 */
public final class References {
  /**
   * Utility class - don't instantiate.
   */
  private References() {
  }

  /**
   * <p>
   * Factory method for simple (unlogged) references.
   * </p>
   * <p>
   * <strong>Usage:</strong>
   * </p>
   * <pre>{@code
   * Reference<String> myString = References.ref();
   * }</pre>
   *
   * @param <T> the type of value contained in the reference
   * @return a reference
   */
  @Nonnull
  public static <T> Reference<T> ref() {
    return new ReferenceImpl<T>();
  }

  /**
   * <p>
   * Create a reference which is self-describing. This is especially meant to be
   * used together with steps logging.
   * </p>
   * <p>
   * <strong>Usage:</strong>
   * </p>
   * <pre>{@code
   * Reference<String> myString = References.ref("aName");
   * }</pre>
   *
   * @param name the name (or description) of the reference used for logging
   * @param <T>  the type of value contained in the reference
   * @return a reference
   */
  @Nonnull
  public static <T> Reference<T> ref(final String name) {
    return new SelfDescribingReferenceImpl<T>(name);
  }
}

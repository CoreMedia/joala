package net.joala.newdata.random;

import javax.annotation.Nonnull;
import javax.inject.Provider;

/**
 * <p>
 *   Builder interface for number providers.
 * </p>
 * @param <T> type of the numbers generated
 * @param <P> type of the number provider created
 * @since 10/23/12
 */
public interface NumberProviderBuilder<T extends Number, P extends Provider<T>>
        extends Provider<T>, FluentNumberRange<T> {
  /**
   * <p>
   * Create a number provider of the given type.
   * </p>
   * @return prepared and configured number provider
   */
  @Nonnull
  P build();
}

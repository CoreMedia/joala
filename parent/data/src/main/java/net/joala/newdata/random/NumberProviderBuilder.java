package net.joala.newdata.random;

import javax.inject.Provider;

/**
 * @since 10/23/12
 */
public interface NumberProviderBuilder<T extends Number, P extends Provider<T>>
        extends Provider<T>, FluentNumberRange<T> {
  P build();
}

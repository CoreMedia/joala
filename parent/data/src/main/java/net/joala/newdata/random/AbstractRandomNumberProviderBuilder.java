package net.joala.newdata.random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.Random;

import static com.google.common.base.Preconditions.checkState;

/**
 * @since 10/24/12
 */
public abstract class AbstractRandomNumberProviderBuilder<T extends Number>
        implements FluentNumberRange<T>,
        NumberProviderBuilder<T, Provider<T>> {
  @Nullable
  private T minValue;
  @Nullable
  private T maxValue;
  @Nullable
  private final Provider<Random> randomProvider;

  public AbstractRandomNumberProviderBuilder(@Nullable final Provider<Random> randomProvider) {
    this.randomProvider = randomProvider;
  }

  @Override
  @Nonnull
  public FluentNumberRange<T> min(@Nonnull final T minValue) {
    checkState(this.minValue == null, "Min Value already set.");
    this.minValue = minValue;
    return this;
  }

  @Override
  @Nonnull
  public FluentNumberRange<T> max(@Nonnull final T maxValue) {
    checkState(this.maxValue == null, "Max Value already set.");
    this.maxValue = maxValue;
    return this;
  }

  protected abstract boolean isValidRange(@Nonnull T lowerBound, @Nonnull T upperBound);

  @Override
  public Provider<T> build() {
    checkState(
            minValue == null
                    || maxValue == null
                    || isValidRange(minValue, maxValue),
            "Maximum value must be greater or equal to minimum value.");
    return newProvider(minValue, maxValue, randomProvider);
  }

  protected abstract Provider<T> newProvider(final T minValue, final T maxValue, final Provider<Random> randomProvider);

  @Override
  public T get() {
    return build().get();
  }
}

package net.joala.newdata.random.impl;

import net.joala.newdata.random.AbstractRandomNumberProvider;
import net.joala.newdata.random.AbstractRandomNumberProviderBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.Random;

/**
 * @since 10/23/12
 */
public class RandomLongProvider extends AbstractRandomNumberProvider<Long> {
  public RandomLongProvider() {
    this(null, null, null);
  }

  public RandomLongProvider(@Nullable final Provider<Random> randomProvider) {
    this(null, null, randomProvider);
  }

  public RandomLongProvider(final Long minValue, final Long maxValue) {
    this(minValue, maxValue, null);
  }

  public RandomLongProvider(@Nullable final Long minValue, @Nullable final Long maxValue,
                            @Nullable final Provider<Random> randomProvider) {
    super(minValue, maxValue, randomProvider);
  }

  @Nonnull
  @Override
  protected Long getMaxDefault() {
    return Long.MAX_VALUE;
  }

  @Nonnull
  @Override
  protected Long getMinDefault() {
    return Long.MIN_VALUE;
  }

  @Nonnull
  @Override
  public Builder min(@Nonnull final Long minValue) {
    return new Builder(getRandomProvider()).min(minValue);
  }

  @Nonnull
  @Override
  public Builder max(@Nonnull final Long maxValue) {
    return new Builder(getRandomProvider()).max(maxValue);
  }

  @Override
  @Nonnull
  protected Long get(@Nonnull final Long minValue, @Nonnull final Long maxValue, final double percentage) {
    return (long) (percentage * maxValue + (1d - percentage) * minValue);
  }

  public static final class Builder extends AbstractRandomNumberProviderBuilder<Long> {
    private Builder(final Provider<Random> randomProvider) {
      super(randomProvider);
    }

    @Override
    protected boolean isValidRange(@Nonnull final Long lowerBound, @Nonnull final Long upperBound) {
      return Long.compare(lowerBound, upperBound) <= 0;
    }

    @Override
    protected Provider<Long> newProvider(final Long minValue, final Long maxValue, final Provider<Random> randomProvider) {
      return new RandomLongProvider(minValue, maxValue, randomProvider);
    }

    @Nonnull
    @Override
    public Builder max(@Nonnull final Long maxValue) {
      super.max(maxValue);
      return this;
    }

    @Nonnull
    @Override
    public Builder min(@Nonnull final Long minValue) {
      super.min(minValue);
      return this;
    }
  }
}

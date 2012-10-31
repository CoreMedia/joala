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
public class RandomIntegerProvider extends AbstractRandomNumberProvider<Integer> {
  public RandomIntegerProvider() {
    this(null, null, null);
  }

  public RandomIntegerProvider(@Nullable final Provider<Random> randomProvider) {
    this(null, null, randomProvider);
  }

  public RandomIntegerProvider(final Integer minValue, final Integer maxValue) {
    this(minValue, maxValue, null);
  }

  public RandomIntegerProvider(@Nullable final Integer minValue, @Nullable final Integer maxValue,
                               @Nullable final Provider<Random> randomProvider) {
    super(minValue, maxValue, randomProvider);
  }

  @Nonnull
  @Override
  protected Integer getMaxDefault() {
    return Integer.MAX_VALUE;
  }

  @Nonnull
  @Override
  protected Integer getMinDefault() {
    return Integer.MIN_VALUE;
  }

  @Nonnull
  @Override
  public Builder min(@Nonnull final Integer minValue) {
    return new Builder(getRandomProvider()).min(minValue);
  }

  @Nonnull
  @Override
  public Builder max(@Nonnull final Integer maxValue) {
    return new Builder(getRandomProvider()).max(maxValue);
  }

  @Override
  @Nonnull
  protected Integer get(@Nonnull final Integer minValue, @Nonnull final Integer maxValue, final double percentage) {
    return (int) (percentage * maxValue + (1d - percentage) * minValue);
  }

  public static final class Builder extends AbstractRandomNumberProviderBuilder<Integer> {
    private Builder(final Provider<Random> randomProvider) {
      super(randomProvider);
    }

    @Override
    @Nonnull
    protected Provider<Integer> newProvider(final Integer minValue, final Integer maxValue, final Provider<Random> randomProvider) {
      return new RandomIntegerProvider(minValue, maxValue, randomProvider);
    }

    @Nonnull
    @Override
    public Builder max(@Nonnull final Integer maxValue) {
      super.max(maxValue);
      return this;
    }

    @Nonnull
    @Override
    public Builder min(@Nonnull final Integer minValue) {
      super.min(minValue);
      return this;
    }
  }
}

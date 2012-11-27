/*
 * Copyright 2012 CoreMedia AG
 *
 * This file is part of Joala.
 *
 * Joala is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Joala is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.newdata.random;

import net.joala.newdata.DataProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.Random;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Optional.fromNullable;

/**
 * <p>
 * Convenience base class for random number providers.
 * </p>
 *
 * @since 10/24/12
 */
public abstract class AbstractRandomNumberProvider<T extends Number>
        implements DataProvider<T>, FluentNumberRange<T> {
  /**
   * Default random provider to use if none has been configured.
   */
  private static final Provider<Random> DEFAULT_RANDOM_PROVIDER = new RandomProvider();

  /**
   * Provider for Random Number Generator. {@code null} means <em>uninitialized</em> thus use the default.
   */
  @Nullable
  private final Provider<Random> randomProvider;
  /**
   * Minimum value. {@code null} = <em>uninitialized</em>; use default
   */
  @Nullable
  private final T minValue;
  /**
   * Maximum value. {@code null} = <em>uninitialized</em>; use default
   */
  @Nullable
  private final T maxValue;

  /**
   * <p>
   * Constructor setting the several values. Especially meant to enable to
   * configure this random number provider through Spring or any other IOC
   * framework. Recommended to override and to make it public in the
   * implementing class.
   * </p>
   *
   * @param minValue       minimum value; {@code null} to use default
   * @param maxValue       maximum value; {@code null} to use default
   * @param randomProvider provider for random numbers; {@code null} to use default
   */
  protected AbstractRandomNumberProvider(@Nullable final T minValue, @Nullable final T maxValue,
                                         @Nullable final Provider<Random> randomProvider) {
    this.maxValue = maxValue;
    this.minValue = minValue;
    this.randomProvider = randomProvider;
  }

  /**
   * <p>
   * Default value to use if required. This value will be used if the maximum value
   * is uninitialized.
   * </p>
   *
   * @return default maximum value
   */
  @Nonnull
  protected abstract T getMaxDefault();

  /**
   * <p>
   * Default value to use if required. This value will be used if the minimum value
   * is uninitialized.
   * </p>
   *
   * @return default minimum value
   */
  @Nonnull
  protected abstract T getMinDefault();

  /**
   * <p>
   * Get a value between the given min and maxvalue where the position on the number line
   * is determined by the percentage. A typical implementation looks like this:
   * </p>
   * <pre>{@code
   * return (some-cast) (percentage * maxValue + (1d - percentage) * minValue);
   * }</pre>
   *
   * @param minValue   minimum value
   * @param maxValue   maximum value
   * @param percentage position on number line
   * @return number calculated from the given parameters
   */
  @Nonnull
  protected abstract T get(@Nonnull final T minValue, @Nonnull final T maxValue, final double percentage);

  /**
   * <p>
   * Retrieve the random provider. Useful for implementing {@link FluentNumberRange#min(Number)} and
   * {@link FluentNumberRange#max(Number)} in order to create a builder which knows of this random
   * provider.
   * </p>
   *
   * @return random number provider, either as configured or the default provider
   */
  @Nonnull
  protected Provider<Random> getRandomProvider() {
    return fromNullable(randomProvider).or(DEFAULT_RANDOM_PROVIDER);
  }

  /**
   * <p>
   * Get the maximum value. Either the configured one or the default.
   * </p>
   *
   * @return maximum value (or default if not set)
   */
  @Nonnull
  private T getMaxValue() {
    return fromNullable(maxValue).or(getMaxDefault());
  }

  /**
   * <p>
   * Get the minimum value. Either the configured one or the default.
   * </p>
   *
   * @return minimum value (or default if not set)
   */
  @Nonnull
  private T getMinValue() {
    return fromNullable(minValue).or(getMinDefault());
  }

  /**
   * <p>
   * Retrieve a random number in a specified range.
   * </p>
   *
   * @return random number
   */
  @Override
  @Nonnull
  public final T get() {
    return get(getMinValue(), getMaxValue(), getRandomProvider().get().nextDouble());
  }

  @Override
  public String toString() {
    return toStringHelper(this)
            .add("minValue", minValue)
            .add("maxValue", maxValue)
            .add("randomProvider", randomProvider)
            .toString();
  }
}

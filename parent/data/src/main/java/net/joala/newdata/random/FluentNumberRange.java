package net.joala.newdata.random;

import javax.annotation.Nonnull;

/**
 * <p>
 * Fluent interface for configuring number ranges for random numbers.
 * </p>
 * <p>
 * <strong>Implementing Interface</strong>
 * </p>
 * <ul>
 * <li>Override the given methods by a return type equal to the implementing class,</li>
 * <li>{@code null} is reserved as special value for <em>uninitialized</em></li>
 * <li><em>uninitialized</em> means to use a convenient default value.</li>
 * </ul>
 *
 * @since 10/23/12
 */
public interface FluentNumberRange<T extends Number> {

  /**
   * <p>
   * Set minimum value the random numbers might reach.
   * </p>
   * @param minValue minimum value
   * @return self reference
   */
  @Nonnull
  FluentNumberRange<T> min(@Nonnull T minValue);

  /**
   * <p>
   * Set maximum value the random numbers might reach.
   * </p>
   * @param maxValue maximum value
   * @return self reference
   */
  @Nonnull
  FluentNumberRange<T> max(@Nonnull T maxValue);
}

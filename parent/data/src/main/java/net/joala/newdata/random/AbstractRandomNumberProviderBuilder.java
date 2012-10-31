package net.joala.newdata.random;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Provider;
import java.util.Random;

import static com.google.common.base.Preconditions.checkState;

/**
 * <p>
 * Abstract builder for random number providers. Intended use: For Builders as
 * inner classes of the NumberProvider implementations.
 * </p>
 *
 * @param <T> type of the numbers generated
 * @since 10/24/12
 */
@NotThreadSafe
public abstract class AbstractRandomNumberProviderBuilder<T extends Number>
        implements FluentNumberRange<T>,
        NumberProviderBuilder<T, Provider<T>> {
  /**
   * The minimum value. Can only be set once.
   */
  @Nullable
  private T minValue;
  /**
   * The maximum value. Can only be set once.
   */
  @Nullable
  private T maxValue;
  /**
   * The number provider to use.
   */
  @Nullable
  private final Provider<Random> randomProvider;

  /**
   * <p>
   * Signals if an instance of a random number provider has already been built.
   * Especially used to block further configuration of this builder when instances
   * have already been built.
   * </p>
   */
  private boolean gotBuilt;

  /**
   * <p>
   * Constructor specifying the random number provider to use.
   * </p>
   *
   * @param randomProvider random number provider to use; {@code null} to use default provider
   */
  protected AbstractRandomNumberProviderBuilder(@Nullable final Provider<Random> randomProvider) {
    this.randomProvider = randomProvider;
  }

  @Override
  @Nonnull
  public FluentNumberRange<T> min(@Nonnull final T minValue) {
    // For if to use checkState here or not see bottom of class.
    checkState(this.minValue == null, "Min Value already set.");
    checkBuiltState();
    this.minValue = minValue;
    return this;
  }

  @Override
  @Nonnull
  public FluentNumberRange<T> max(@Nonnull final T maxValue) {
    // For if to use checkState here or not see bottom of class.
    checkState(this.maxValue == null, "Max Value already set.");
    checkBuiltState();
    this.maxValue = maxValue;
    return this;
  }

  /**
   * Fails with exception if a random number provider already got created.
   * In this case it is expected that further configuration is prohibited.
   */
  protected final void checkBuiltState() {
    checkState(!gotBuilt, "An instance of the random number provider already got created. Further configuration " +
            "prohibited.");
  }

  @Override
  @Nonnull
  public Provider<T> build() {
    gotBuilt = true;
    // For if to use checkState here to prevent multiple calls to build see bottom of class.
    return newProvider(minValue, maxValue, randomProvider);
  }

  /**
   * <p>
   * Creates a new instance of the previously configured provider.
   * </p>
   *
   * @param minValue       minimum value of random number range; might be {@code null}
   * @param maxValue       maximum value of random number range; might be {@code null}
   * @param randomProvider random number provider to use; might be {@code null}
   * @return a new provider as configured
   */
  @Nonnull
  protected abstract Provider<T> newProvider(@Nullable final T minValue, @Nullable final T maxValue,
                                             @Nullable final Provider<Random> randomProvider);

  @Override
  public T get() {
    return build().get();
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("minValue", minValue)
            .add("maxValue", maxValue)
            .add("randomProvider", randomProvider)
            .add("gotBuilt", gotBuilt)
            .toString();
  }
}

/*
 * Architectural Decisions:
 *
 * - Using checkState() for duplicate calls to fluent interface methods.
 *
 *   Reusing this builder to provide different configured number providers might be irritating for
 *   readers of the code.
 *
 * - Not using checkState() for build()
 *
 *   Unsure here. Actually it does no harm to understand the code if the builder is used to provide
 *   multiple instances of providers which are configured alike and thus are considered to be equal.
 */
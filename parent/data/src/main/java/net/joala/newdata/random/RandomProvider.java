package net.joala.newdata.random;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.Random;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Optional.fromNullable;

/**
 * <p>
 * Provides a random number generator with a given seed. The seed
 * defaults to {@link System#currentTimeMillis()}.
 * </p>
 *
 * @since 10/23/12
 */
public class RandomProvider implements Provider<Random> {
  /**
   * Logging instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(RandomProvider.class);

  /**
   * Seed for random number generator.
   */
  @Nullable
  private final Long seed;

  /**
   * Constructor with default seed.
   */
  public RandomProvider() {
    this(null);
  }

  /**
   * Constructor with given seed.
   *
   * @param seed provided seed; {@code null} for default seed
   */
  public RandomProvider(@Nullable final Long seed) {
    this.seed = seed;
  }

  /**
   * <p>
   * Retrieve a random number generator.
   * </p>
   *
   * @return random number generator
   */
  @Override
  public Random get() {
    final long usedSeed = fromNullable(seed).or(System.currentTimeMillis());
    // For tests it is important to log the seed here in order to allow tests
    // to rerun with the same seed for debugging purpose.
    LOG.info("Creating random number generator with seed: {}", usedSeed);
    return new Random(usedSeed);
  }

  @Override
  public String toString() {
    return toStringHelper(this)
            .add("seed", seed)
            .toString();
  }
}

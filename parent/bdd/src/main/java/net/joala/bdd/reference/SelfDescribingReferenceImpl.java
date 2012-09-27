package net.joala.bdd.reference;

import com.google.common.base.Objects;
import org.hamcrest.Description;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p>
 * Default implementation of {@link SelfDescribingReference}.
 * </p>
 *
 * @since 6/21/12
 */
public class SelfDescribingReferenceImpl<T> extends ReferenceImpl<T> implements SelfDescribingReference<T> {
  @Nullable
  private final String name;

  public SelfDescribingReferenceImpl(@Nullable final String name) {
    this.name = name;
  }

  @Override
  @Nullable
  public String getName() {
    return name;
  }

  @Override
  public void describeTo(@Nonnull final Description description) {
    description.appendText(name != null ? getName() : "<noname>");
    description.appendText("=");
    describeValue(description);
  }

  /**
   * <p>
   * Describe the value of this reference. Override if you want to describe the value on your own.
   * Mind that you must call {@link #hasValue()} before retrieving the value via {@link #get()} as {@link #get()}
   * will fail with exception if no value has been set yet.
   * </p>
   *
   * @param description description to append the value to
   */
  protected void describeValue(@Nonnull final Description description) {
    if (hasValue()) {
      description.appendValue(get());
    } else {
      description.appendText("<none>");
    }
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("name", name)
            .toString();
  }
}

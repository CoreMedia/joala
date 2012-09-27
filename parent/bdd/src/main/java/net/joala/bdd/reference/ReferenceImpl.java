package net.joala.bdd.reference;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * <p>
 * Implementation of {@link Reference}.
 * </p>
 *
 * @since 6/5/12
 */
public class ReferenceImpl<T> implements Reference<T> {

  private T value;
  private final Map<String, Object> properties = new HashMap<String, Object>(1);

  @Override
  public void set(@Nonnull final T value) {
    checkNotNull(value, "Reference value must not be null.");
    if (hasValue()) {
      throw new ReferenceAlreadyBoundException(format("Reference already bound to value %s of type %s.", this.value, this.value.getClass()));
    }
    this.value = value;
  }

  @Override
  @Nonnull
  public T get() {
    if (!hasValue()) {
      throw new ReferenceNotBoundException("Reference not bound to any value.");
    }
    return value;
  }

  /**
   * Make it possible to verify if this reference has a value.
   *
   * @return if this reference has a value
   */
  protected final boolean hasValue() {
    return value != null;
  }

  @Override
  public void setProperty(@Nonnull final String key, @Nullable final Object value) {
    checkNotNull(key, "Property key must not be null.");
    if (properties.containsKey(key)) {
      throw new PropertyAlreadySetException(format("Property '%s' already set to value %s", key, properties.get(key)));
    }
    properties.put(key, value);
  }

  @Override
  public Object getProperty(@Nonnull final String key) {
    checkNotNull(key, "Property key must not be null.");
    if (!properties.containsKey(key)) {
      throw new PropertyNotSetException(format("Property '%s' not set.", key));
    }
    return properties.get(key);
  }

  @Override
  @Nullable
  public <P> P getProperty(@Nonnull final String key, @Nonnull final Class<P> clazz) {
    checkNotNull(key, "Property key must not be null.");
    final Object propertyValue = getProperty(key);
    if (propertyValue == null) {
      return null;
    }
    return clazz.cast(propertyValue);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("value", value)
            .add("properties", properties)
            .toString();
  }
}

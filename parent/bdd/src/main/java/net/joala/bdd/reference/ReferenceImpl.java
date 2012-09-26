package net.joala.bdd.reference;

import java.util.HashMap;
import java.util.Map;

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
  public void set(final T value) {
    if (value == null) {
      throw new NullPointerException("Reference value must not be null.");
    }
    if (this.value != null) {
      throw new ReferenceAlreadyBoundException(format("Reference already bound to value %s of type %s.", this.value, this.value.getClass()));
    }
    this.value = value;
  }

  @Override
  public T get() {
    if (value == null) {
      throw new ReferenceNotBoundException("Reference not bound to any value.");
    }
    return value;
  }

  protected boolean hasValue() {
    return value != null;
  }

  @Override
  public void setProperty(final String key, final Object value) {
    if (key == null) {
      throw new NullPointerException("Property key must not be null.");
    }
    if (properties.containsKey(key)) {
      throw new PropertyAlreadySetException(format("Property '%s' already set to value %s", key, properties.get(key)));
    }
    properties.put(key, value);
  }

  @Override
  public Object getProperty(final String key) {
    if (!properties.containsKey(key)) {
      throw new PropertyNotSetException(format("Property '%s' not set.", key));
    }
    return properties.get(key);
  }

  @Override
  public <P> P getProperty(final String key, final Class<P> clazz) {
    final Object propertyValue = getProperty(key);
    if (propertyValue == null) {
      return null;
    }
    return clazz.cast(propertyValue);
  }
}

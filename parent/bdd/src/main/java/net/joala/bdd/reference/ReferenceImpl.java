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

package net.joala.bdd.reference;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

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

  private ValueHolder<T> valueHolder;

  private final Map<String, Object> properties = new HashMap<String, Object>(1);

  @Override
  public void set(@Nullable final T value) {
    if (hasValue()) {
      final T oldValue = this.valueHolder.get();
      throw new ReferenceAlreadyBoundException(format("Reference already bound to value %s.", oldValue));
    }
    this.valueHolder = new ValueHolder<T>(value);
  }

  @Override
  public T get() {
    if (!hasValue()) {
      throw new ReferenceNotBoundException("Reference not bound to any value.");
    }
    return valueHolder.get();
  }

  @Nonnull
  @Override
  public T getNonNull() {
    return checkNotNull(get(), "Value must not be null.");
  }

  /**
   * Make it possible to verify if this reference has a value.
   *
   * @return if this reference has a value
   */
  protected final boolean hasValue() {
    return valueHolder != null;
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
            .add("valueHolder", valueHolder)
            .add("properties", properties)
            .toString();
  }

  private static final class ValueHolder<T> {
    @Nullable
    private final T value;

    private ValueHolder(@Nullable final T value) {
      this.value = value;
    }

    @Nullable
    public T get() {
      return value;
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this)
              .add("value", value)
              .toString();
    }
  }

}

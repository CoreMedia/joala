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
 * along with Joala.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.bdd.reference;

import com.google.common.base.MoreObjects;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * <p>
 * Implementation of {@link Reference}.
 * </p>
 *
 * @see net.joala.bdd
 * @since 6/5/12
 */
public class ReferenceImpl<T> implements Reference<T> {
  private boolean valueSet;
  private T value;
  private final Map<String, Object> properties = new HashMap<String, Object>(1);

  @Override
  public void set(@Nullable final T value) {
    if (hasValue()) {
      throw new ReferenceAlreadyBoundException(format("Reference already bound to value %s of type %s.", this.value, this.value.getClass()));
    }
    valueSet = true;
    this.value = value;
  }

  @Override
  @Nullable
  public T get() {
    if (!hasValue()) {
      throw new ReferenceNotBoundException("Reference not bound to any value.");
    }
    return value;
  }

  @Override
  public boolean hasValue() {
    return valueSet;
  }

  @Override
  public void setProperty(@NonNull final String key, @Nullable final Object value) {
    checkNotNull(key, "Property key must not be null.");
    if (hasProperty(key)) {
      throw new PropertyAlreadySetException(format("Property '%s' already set to value %s", key, properties.get(key)));
    }
    properties.put(key, value);
  }

  @Override
  public Object getProperty(@NonNull final String key) {
    checkNotNull(key, "Property key must not be null.");
    if (!hasProperty(key)) {
      throw new PropertyNotSetException(format("Property '%s' not set.", key));
    }
    return properties.get(key);
  }

  @Override
  @Nullable
  public <P> P getProperty(@NonNull final String key, @NonNull final Class<P> clazz) {
    checkNotNull(key, "Property key must not be null.");
    checkNotNull(clazz, "Expected class must not be null.");
    return clazz.cast(getProperty(key));
  }

  @Override
  public boolean hasProperty(@NonNull final String key) {
    checkNotNull(key, "Property key must not be null.");
    return properties.containsKey(key);
  }

  @Override
  public Object removeProperty(@NonNull final String key) {
    checkNotNull(key, "Property key must not be null.");
    if (!hasProperty(key)) {
      throw new PropertyNotSetException(format("Property '%s' not set.", key));
    }
    return properties.remove(key);
  }

  @Override
  @Nullable
  public <P> P removeProperty(@NonNull final String key, @NonNull final Class<P> expectedClass) {
    checkNotNull(key, "Property key must not be null.");
    checkNotNull(expectedClass, "Expected class must not be null.");
    return expectedClass.cast(removeProperty(key));
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("value", value)
            .add("valueSet", valueSet)
            .add("properties", properties)
            .toString();
  }
}

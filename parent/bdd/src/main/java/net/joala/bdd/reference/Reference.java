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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <p>
 * A reference is meant to be used to be shared among steps of BDD tests written in JUnit. It is typically
 * created before a test, filled once with a value during the tests and then can be read multiple times.
 * </p>
 * <p>
 * References can hold properties that can be used for later assertions.
 * </p>
 *
 * @param <T> the type of the reference value
 * @since 6/5/12
 * @see net.joala.bdd
 */
public interface Reference<T> {
  /**
   * <p>
   * Set reference value. Can only be called once.
   * </p>
   *
   * @param value the value of the reference
   * @throws ReferenceAlreadyBoundException if the reference already has a value
   * @throws NullPointerException           if the value set is null
   */
  void set(@Nonnull T value);

  /**
   * <p>
   * Retrieve the value of a reference.
   * </p>
   *
   * @return the value of the reference
   * @throws ReferenceNotBoundException if the reference does not contain a value yet
   */
  @Nonnull
  T get();

  /**
   * <p>
   * Set a property which belongs to the reference value. Such a property might for example be a text which will
   * be represent the reference value in a webapp. For example a username or something similar.
   * </p>
   *
   * @param key   the name of the property
   * @param value the value of the property
   * @throws NullPointerException        if the key is {@code null}
   * @throws PropertyAlreadySetException if you already set this property
   */
  void setProperty(@Nonnull String key, @Nullable Object value);

  /**
   * <p>
   * Read the property value.
   * </p>
   *
   * @param key the name of the property
   * @return property value
   * @throws PropertyNotSetException if you did not define that property before
   */
  @Nullable
  Object getProperty(@Nonnull String key);

  /**
   * <p>
   * Read the property value.
   * </p>
   *
   * @param key   the name of the property
   * @param clazz the type of the property value to cast it to
   * @param <P>   the type of the property value
   * @return property value
   * @throws PropertyNotSetException if you did not define that property before
   */
  @Nullable
  <P> P getProperty(@Nonnull String key, @Nonnull Class<P> clazz);

  /**
   * <p>
   * Checks, whether a property with the given key is set.
   * </p>
   *
   * @param key the name of the property
   * @return <code>true</code> if the property is set, otherwise <code>false</code>
   */
  boolean hasProperty(@Nonnull String key);

  /**
   * Removes the property with the given key if it exists, otherwise does nothing.
   *
   * @param key the name of the property
   * @param expectedClass the type of the property value to cast it to
   * @param <P> the type of the proeprty value
   * @return the property value if it was set, otherwise <code>null</code>.
   */
  @Nullable
  <P> P removeProperty(@Nonnull String key, @Nonnull Class<P> expectedClass);
}

package net.joala.bdd.reference;

/**
 * <p>
 * A reference is meant to be used to be shared among steps of BDD tests written in JUnit. It is typically
 * created before a test, filled once with a value during the tests and then can be read multiple times.
 * </p>
 *
 * @param <T> the type of the reference value
 * @since 6/5/12
 */
public interface Reference<T> {
  /**
   * <p>
   * Set reference value. Can only be called once.
   * </p>
   *
   * @param value the value of the reference
   * @throws ReferenceAlreadyBoundException if the reference already has a value
   * @throws NullPointerException         if the value set is null
   */
  void set(T value);

  /**
   * <p>
   * Retrieve the value of a reference.
   * </p>
   *
   * @return the value of the reference
   * @throws ReferenceNotBoundException if the reference does not contain a value yet
   */
  T get();

  /**
   * <p>
   * Set a property which belongs to the reference value. Such a property might for example be a text which will
   * be represent the reference value in a webapp. For example a username or something similar.
   * </p>
   *
   * @param key   the name of the property
   * @param value the value of the property
   * @param <P>   the property value's type
   * @throws NullPointerException if the key is {@code null}
   * @throws PropertyAlreadySetException if you already set this property
   */
  <P> void setProperty(String key, P value);

  /**
   * <p>
   * Read the property value.
   * </p>
   *
   * @param key the name of the property
   * @return property value
   * @throws PropertyNotSetException if you did not define that property before
   */
  Object getProperty(String key);

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
  <P> P getProperty(String key, Class<P> clazz);
}

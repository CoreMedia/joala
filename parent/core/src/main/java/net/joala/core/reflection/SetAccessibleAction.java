package net.joala.core.reflection;

import com.google.common.base.Objects;

import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedAction;

/**
 * Privileged action to set something to accessible.
 * @param <T> the type of the accessible object
 * @since 10/8/12
 */
public class SetAccessibleAction<T extends AccessibleObject> implements PrivilegedAction<Void> {
  private final T object;

  /**
   * Creates privileged action bound to the given accessible object.
   * @param object object to make accessible
   */
  public SetAccessibleAction(final T object) {
    this.object = object;
  }

  @Override
  public Void run() {
    object.setAccessible(true);
    return null;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("object", object)
            .toString();
  }
}

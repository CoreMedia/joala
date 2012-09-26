package net.joala.bdd.reference;

import org.hamcrest.SelfDescribing;

/**
 * <p>
 * Self describable references may be printed to log along with the steps where they are used.
 * </p>
 *
 * @since 6/21/12
 */
public interface SelfDescribingReference<T> extends SelfDescribing, Reference<T> {
  /**
   * Retrieve the name of this reference.
   * @return name
   */
  String getName();
}

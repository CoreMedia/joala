package net.joala.dns;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static net.joala.dns.NameStore.nameStore;

/**
 * <p>
 *   Use this test watcher in order to ensure that tests modifying the DNS
 *   entries don't influence each other.
 * </p>
 * <p>
 *   In order to use this test watcher add this to your test class (or superclass):
 * </p>
 * <pre>{@code
 *   &#64;Rule
 *   public final TestWatcher nameStoreTestWatcher = new NameStoreTestWatcher();
  * }</pre>
 * @since 10/6/12
 */
public class NameStoreTestWatcher extends TestWatcher {
  /**
   * Will clear the namestore.
   * @param description unused
   */
  @Override
  protected void starting(final Description description) {
    nameStore().ensureJoalaDnsInstalled();
    nameStore().clear();
  }

  /**
   * Will clear the namestore.
   * @param description unused
   */
  @Override
  protected void finished(final Description description) {
    nameStore().clear();
  }
}

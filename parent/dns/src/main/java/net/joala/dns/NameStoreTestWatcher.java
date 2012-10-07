package net.joala.dns;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static net.joala.dns.NameStore.nameStore;

/**
 * <p>
 * Use this test watcher in order to ensure that tests modifying the DNS
 * entries don't influence each other.
 * </p>
 * <p>
 * In order to use this test watcher add this to your test class (or superclass):
 * </p>
 * <pre>{@code
 *   &#64;Rule
 *   public final TestWatcher nameStoreTestWatcher = new NameStoreTestWatcher();
 * }</pre>
 * <p>
 * In addition to clearing a name store the test watcher also ensures that the
 * name store is installed. If not it will prevent tests from running by throwing
 * an assumption failure.
 * </p>
 *
 * @since 10/6/12
 */
public class NameStoreTestWatcher extends TestWatcher {
  /**
   * Enforcer used to install Joala DNS if it is not yet done.
   */
  private static final LocalDNSNameServiceEnforcer ENFORCER = new LocalDNSNameServiceEnforcer(nameStore());

  /**
   * Will clear the namestore.
   *
   * @param description unused
   */
  @Override
  protected void starting(final Description description) {
    ENFORCER.ensureJoalaDnsInstalled();
    nameStore().clear();
  }

  /**
   * Will clear the namestore.
   *
   * @param description unused
   */
  @Override
  protected void finished(final Description description) {
    nameStore().clear();
  }

}

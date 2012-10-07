package net.joala.dns.story;

import net.joala.bdd.watcher.JUnitScenarioWatcher;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @since 10/6/12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/joala/StoryTest-context.xml")
public abstract class StoryBaseTest {
  @Rule
  public final TestWatcher testWatcher = new JUnitScenarioWatcher();
}

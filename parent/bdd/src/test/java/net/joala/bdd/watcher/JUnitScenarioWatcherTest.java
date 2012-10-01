package net.joala.bdd.watcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;
import static org.junit.runner.JUnitCore.runClasses;

/**
 * <p>
 * Tests {@link JUnitScenarioWatcher}.
 * </p>
 *
 * @since 10/1/12
 */
public class JUnitScenarioWatcherTest {

  private static final Pattern LOGGED_MESSAGE_PATTERN = Pattern.compile("^([^:]+: \\.+ )\\S.*$");
  private static final Pattern NEWLINE_PATTERN = Pattern.compile("[\n]");
  private static final String STORY_HEADING = "story";
  private static final String SCENARIO_HEADING = "scenario";

  public static class ViolatedAssumptionTest {
    private static Appendable watchedLog = new StringBuilder();

    @Rule
    public TestRule watcher = new MockJUnitScenarioWatcher(watchedLog);

    @Test
    public void scenario_assumption_failure() {
      assumeTrue(false);
    }
  }

  @Test
  public void assumptionFailureScenarioStateShouldBeReported() {
    ViolatedAssumptionTest.watchedLog = new StringBuilder();
    scenarioStateShouldBeReported(ViolatedAssumptionTest.watchedLog, ViolatedAssumptionTest.class, "skipped");
  }

  @Test
  public void assumptionFailureScenarioStoryHeadersShouldBeReported() {
    ViolatedAssumptionTest.watchedLog = new StringBuilder();
    scenarioStoryHeadersShouldBeReported(ViolatedAssumptionTest.watchedLog, ViolatedAssumptionTest.class);
  }

  @Test
  public void assumptionFailureScenarioStoryHeadersShouldBeAligned() {
    ViolatedAssumptionTest.watchedLog = new StringBuilder();
    headersShouldBeAligned(ViolatedAssumptionTest.watchedLog, ViolatedAssumptionTest.class);
  }

  @Test
  public void assumptionFailureScenarioTitleShouldBeReported() {
    ViolatedAssumptionTest.watchedLog = new StringBuilder();
    titleShouldBeReported(ViolatedAssumptionTest.watchedLog, ViolatedAssumptionTest.class, "assumption failure");
  }

  @Test
  public void assumptionFailureStoryTitleShouldBeReported() {
    ViolatedAssumptionTest.watchedLog = new StringBuilder();
    titleShouldBeReported(ViolatedAssumptionTest.watchedLog, ViolatedAssumptionTest.class, "violated assumption");
  }

  public static class AssertionErrorTest {
    private static Appendable watchedLog = new StringBuilder();

    @Rule
    public TestRule watcher = new MockJUnitScenarioWatcher(watchedLog);

    @Test
    public void scenario_assertion_should_fail() {
      fail();
    }
  }

  @Test
  public void assertionErrorScenarioStateShouldBeReported() {
    AssertionErrorTest.watchedLog = new StringBuilder();
    scenarioStateShouldBeReported(AssertionErrorTest.watchedLog, AssertionErrorTest.class, "failed");
  }

  @Test
  public void assertionErrorScenarioStoryHeadersShouldBeReported() {
    AssertionErrorTest.watchedLog = new StringBuilder();
    scenarioStoryHeadersShouldBeReported(AssertionErrorTest.watchedLog, AssertionErrorTest.class);
  }

  @Test
  public void assertionErrorScenarioStoryHeadersShouldBeAligned() {
    AssertionErrorTest.watchedLog = new StringBuilder();
    headersShouldBeAligned(AssertionErrorTest.watchedLog, AssertionErrorTest.class);
  }

  @Test
  public void assertionErrorScenarioTitleShouldBeReported() {
    AssertionErrorTest.watchedLog = new StringBuilder();
    titleShouldBeReported(AssertionErrorTest.watchedLog, AssertionErrorTest.class, "assertion should fail");
  }

  @Test
  public void assertionErrorStoryTitleShouldBeReported() {
    AssertionErrorTest.watchedLog = new StringBuilder();
    titleShouldBeReported(AssertionErrorTest.watchedLog, AssertionErrorTest.class, "assertion error");
  }

  public static class PassedTest {
    private static Appendable watchedLog = new StringBuilder();

    @Rule
    public TestRule watcher = new MockJUnitScenarioWatcher(watchedLog);

    @Test
    public void scenario_should_pass() {
    }
  }

  @Test
  public void passedScenarioStateShouldBeReported() {
    PassedTest.watchedLog = new StringBuilder();
    scenarioStateShouldBeReported(PassedTest.watchedLog, PassedTest.class, "success");
  }

  @Test
  public void passedScenarioStoryHeadersShouldBeReported() {
    PassedTest.watchedLog = new StringBuilder();
    scenarioStoryHeadersShouldBeReported(PassedTest.watchedLog, PassedTest.class);
  }

  @Test
  public void passedScenarioStoryHeadersShouldBeAligned() {
    PassedTest.watchedLog = new StringBuilder();
    headersShouldBeAligned(PassedTest.watchedLog, PassedTest.class);
  }

  @Test
  public void passedScenarioTitleShouldBeReported() {
    PassedTest.watchedLog = new StringBuilder();
    titleShouldBeReported(PassedTest.watchedLog, PassedTest.class, "should pass");
  }

  @Test
  public void passedStoryTitleShouldBeReported() {
    PassedTest.watchedLog = new StringBuilder();
    titleShouldBeReported(PassedTest.watchedLog, PassedTest.class, "passed");
  }

  private void scenarioStateShouldBeReported(final Appendable log, final Class<?> testClass, final String expectedState) {
    runClasses(testClass);
    final String logged = log.toString().toLowerCase();
    assertThat(String.format("Scenario should be marked as %s.", expectedState), logged, containsString(expectedState));
  }

  private void titleShouldBeReported(final Appendable log, final Class<?> testClass, final String expectedTitle) {
    runClasses(testClass);
    final String logged = log.toString().toLowerCase();
    assertThat(String.format("Title %s should be contained.", expectedTitle), logged, containsString(expectedTitle));
  }

  private void headersShouldBeAligned(final Appendable log, final Class<?> testClass) {
    runClasses(testClass);
    final String logged = log.toString().toLowerCase();
    final String[] strings = NEWLINE_PATTERN.split(logged);
    assertEquals("Three lines should be contained in the report.", 3, strings.length);
    String referenceLine = null;
    for (final String string : strings) {
      final Matcher matcher = LOGGED_MESSAGE_PATTERN.matcher(string);
      assumeTrue(matcher.find());
      if (referenceLine == null) {
        referenceLine = matcher.group(1);
      } else {
        final String currentLine = matcher.group(1);
        assertEquals(String.format("Padding for all lines should be equal (reference: %s, current: %s)", referenceLine, currentLine), referenceLine.length(), currentLine.length());
      }
    }
  }

  private void scenarioStoryHeadersShouldBeReported(final Appendable log, final Class<?> testClass) {
    runClasses(testClass);
    final String logged = log.toString().toLowerCase();
    assertThat("Story header should be logged.", logged, containsString(STORY_HEADING));
    assertThat("Scenario header should be logged.", logged, containsString(SCENARIO_HEADING));
  }

  private static final class MockJUnitScenarioWatcher extends JUnitScenarioWatcher {
    private final Appendable appendable;

    private MockJUnitScenarioWatcher(final Appendable appendable) {
      this.appendable = appendable;
    }

    @Override
    protected void report(final String message) {
      try {
        appendable.append(message);
        appendable.append(System.getProperty("line.separator", "\n"));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}

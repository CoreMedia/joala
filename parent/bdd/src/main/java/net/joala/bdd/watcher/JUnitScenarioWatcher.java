package net.joala.bdd.watcher;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * <p>
 * This watcher will log information on stories and scenarios executed. It assumes that
 * the story is the name of the test case while the scenario is the name of the test
 * method. While Story Names are expected to be written in camel-case scenarios must
 * contain the string "scenario" otherwise they won't be logged.
 * </p>
 * <p>
 * Add to your tests using:
 * </p>
 * <pre>{@code
 * &#64;Rule
 * public final TestWatcher testWatcher = new JUnitScenarioWatcher();
 * }
 * </pre>
 *
 * @since 6/2/12
 */
public class JUnitScenarioWatcher extends ExtendedTestWatcher {
  private static final Logger LOG = LoggerFactory.getLogger(JUnitScenarioWatcher.class);
  private static final Pattern INSERT_SPACE_BEFORE_CAP_LETTERS_PATTERN = Pattern.compile("([A-Z][^A-Z0-9]*|[0-9]+)");
  private static final Pattern REMOVE_TEST_SUFFIX_PATTERN = Pattern.compile("I?Test$");

  private String formatStory(final CharSequence rawStory) {
    final String withoutTest = REMOVE_TEST_SUFFIX_PATTERN.matcher(rawStory).replaceFirst("");
    return INSERT_SPACE_BEFORE_CAP_LETTERS_PATTERN.matcher(withoutTest).replaceAll(" $1").trim();
  }

  @Nonnull
  private String formatScenario(@Nonnull final String rawScenario) {
    return rawScenario.replace('_', ' ').replace("scenario", "").trim();
  }

  @Override
  protected void starting(@Nonnull final Description description) {
    if (isScenario(description)) {
      LOG.info("STORY: ...... {}", formatStory(description.getTestClass().getSimpleName()));
      LOG.info("SCENARIO: ... {}", formatScenario(description.getMethodName()));
    }
  }

  @Override
  protected void failed(final Throwable e, @Nonnull final Description description) {
    if (isScenario(description)) {
      LOG.info("SCENARIO: ... {} (FAILED)", formatScenario(description.getMethodName()));
    }
  }

  @Override
  protected void skipped(final AssumptionViolatedException e, @Nonnull final Description description) {
    if (isScenario(description)) {
      LOG.info("SCENARIO: ... {} (SKIPPED)", formatScenario(description.getMethodName()));
    }
  }

  @Override
  protected void succeeded(@Nonnull final Description description) {
    if (isScenario(description)) {
      LOG.info("SCENARIO: ... {} (SUCCESS)", formatScenario(description.getMethodName()));
    }
  }

  private boolean isScenario(@Nonnull final Description description) {
    return description.getMethodName().contains("scenario");
  }

}

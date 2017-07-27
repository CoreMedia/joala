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

package net.joala.bdd.watcher;

import com.google.common.base.Strings;
import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Formatter;
import java.util.regex.Pattern;

import static java.lang.Math.max;

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
 * @see net.joala.bdd
 * @since 6/2/12
 */
@Named
@Singleton
public class JUnitScenarioWatcher extends TestWatcher {
  /**
   * Logging instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(JUnitScenarioWatcher.class);
  /**
   * Pattern to insert spaces between words in test class names. Word delimiters are uppercase letters
   */
  private static final Pattern INSERT_SPACE_BEFORE_CAP_LETTERS_PATTERN = Pattern.compile("([A-Z][^A-Z0-9]*|[0-9]+)");
  /**
   * Pattern to remove the test-suffic from test class names.
   */
  private static final Pattern REMOVE_TEST_SUFFIX_PATTERN = Pattern.compile("I?Test$");
  private static final int MIN_DOTS = 3;
  private static final int MAX_TYPE_PADDING = 15;
  /**
   * Label for the Story aka test class as prefix in the logging output.
   */
  private static final String STORY_HEADING = "Story";
  /**
   * Label for the Scenario aka test method as prefix in the logging output.
   */
  private static final String SCENARIO_HEADING = "Scenario";

  /**
   * Format the story/test class name for output.
   *
   * @param rawStory raw name of the story/test class
   * @return story name ready for output
   */
  private String formatStory(final CharSequence rawStory) {
    final String withoutTest = REMOVE_TEST_SUFFIX_PATTERN.matcher(rawStory).replaceFirst("");
    return INSERT_SPACE_BEFORE_CAP_LETTERS_PATTERN.matcher(withoutTest).replaceAll(" $1").trim();
  }

  /**
   * Format the scenario/test method name for output.
   *
   * @param rawScenario raw name of the scenario/test method
   * @return scenario name ready for output
   */
  @Nonnull
  private String formatScenario(@Nonnull final String rawScenario) {
    return rawScenario.replace('_', ' ').replace("scenario", "").trim();
  }

  @Override
  protected void starting(@Nonnull final Description description) {
    if (isScenario(description)) {
      reportStart(STORY_HEADING, formatStory(description.getTestClass().getSimpleName()));
      reportStart(SCENARIO_HEADING, formatScenario(description.getMethodName()));
    }
  }

  @Override
  protected void skipped(final AssumptionViolatedException e, final Description description) {
    if (isScenario(description)) {
      reportEnd(SCENARIO_HEADING, formatScenario(description.getMethodName()), "skipped");
    }
  }

  @Override
  protected void failed(final Throwable e, @Nonnull final Description description) {
    if (isScenario(description)) {
      reportEnd(SCENARIO_HEADING, formatScenario(description.getMethodName()), "failed");
    }
  }

  @Override
  protected void succeeded(@Nonnull final Description description) {
    if (isScenario(description)) {
      reportEnd(SCENARIO_HEADING, formatScenario(description.getMethodName()), "success");
    }
  }

  /**
   * Determine if the given test matches the scenario-name-pattern.
   *
   * @param description description to retrieve the method name from
   * @return true, if the current test method is identified as scenario
   */
  private boolean isScenario(@Nonnull final Description description) {
    return description.getMethodName().contains("scenario");
  }

  /**
   * Report start of scenario/story.
   *
   * @param type    either story or scenario
   * @param message the message to print
   */
  protected void reportStart(final String type, final String message) {
    try (Formatter formatter = new Formatter()) {
      report(formatter.format("%1$S: %2$s %3$s", type, typePadding(type), message).toString());
    }
  }

  /**
   * Report end of scenario/story.
   *
   * @param type    either story or scenario
   * @param message the message to print
   * @param result  String describing result of finished story/scenario
   */
  protected void reportEnd(final String type, final String message, final String result) {
    try (Formatter formatter = new Formatter()) {
      report(formatter.format("%1$S: %2$s %3$s (%4$S)", type, typePadding(type), message, result).toString());
    }
  }

  /**
   * Report the given message.
   *
   * @param message message to print
   */
  protected void report(final String message) {
    LOG.info(message);
  }

  /**
   * Add some padding to the type to be logged (scenario/story).
   *
   * @param type scenario or story
   * @return padded story/scenario name
   */
  private static String typePadding(final CharSequence type) {
    return Strings.repeat(".", max(MIN_DOTS, MAX_TYPE_PADDING - type.length()));
  }
}

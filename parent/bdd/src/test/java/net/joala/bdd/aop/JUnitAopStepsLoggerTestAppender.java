package net.joala.bdd.aop;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Used to trace logging events during tests.
 *
 * @since 6/2/12
 */
public class JUnitAopStepsLoggerTestAppender extends AppenderBase<ILoggingEvent> {
  private static final Collection<ILoggingEvent> events = new ArrayList<ILoggingEvent>(1);

  @Override
  protected void append(final ILoggingEvent eventObject) {
    events.add(eventObject);
  }

  public static void clearEvents() {
    events.clear();
  }

  public static Collection<ILoggingEvent> getEvents() {
    return Collections.unmodifiableCollection(events);
  }
}

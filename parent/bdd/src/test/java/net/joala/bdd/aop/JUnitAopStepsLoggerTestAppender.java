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

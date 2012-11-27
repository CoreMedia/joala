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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.dns;

import com.google.common.base.Objects;
import org.xbill.DNS.Options;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintStream;
import java.util.Date;
import java.util.Formatter;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * <p>
 * The DNS classes must not use a normal logging framework as all of them
 * might require DNS lookup in their initialization phase. That's why Joala DNS
 * comes with its own logger. It is controlled by the class {@link Options}
 * by DNS-Java library. Thus to make it verbose you should call:
 * </p>
 * <pre>{@code
 *   Options.set("verbose");
 * }</pre>
 * <p>
 * or set the property {@code dnsjava.options} to {@code verbose}.
 * </p>
 *
 * @since 10/6/12
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
final class SystemLogger {
  /**
   * The name of the logger.
   */
  private final String name;

  /**
   * Constructor providing the name of the logger.
   *
   * @param name logger name
   */
  private SystemLogger(final String name) {
    this.name = name;
  }

  /**
   * Creates a logger for the specified class.
   *
   * @param clazz class for which to install the logger
   * @return logger
   */
  static SystemLogger getLogger(final Class<?> clazz) {
    return new SystemLogger(clazz.getSimpleName());
  }

  /**
   * <p>
   * Logs message to stdout with INFO-label.
   * </p>
   *
   * @param msg message to log
   */
  public void info(final String msg) {
    println(out, "info", msg, null);
  }

  /**
   * <p>
   * Logs message to stdout with INFO-label.
   * </p>
   *
   * @param msg message to log
   *            @param th throwable to print the stacktrace of
   */
  public void info(final String msg, final Throwable th) {
    println(out, "info", msg, th);
  }

  /**
   * <p>
   * Logs message to stdout with WARN-label.
   * </p>
   *
   * @param msg message to log
   */
  public void warn(final String msg) {
    println(err, "warn", msg, null);
  }

  /**
   * <p>
   * Logs message to stdout with WARN-label.
   * </p>
   *
   * @param msg message to log
   *            @param th throwable to print the stacktrace of
   */
  public void warn(final String msg, final Throwable th) {
    println(err, "warn", msg, th);
  }

  /**
   * <p>
   * Will format and print the message.
   * </p>
   * @param stream the stream to write to
   * @param level the log level; only used for the message
   * @param message the message
   * @param th the throwable to log the stack trace of; {@code null} for none
   */
  private void println(@Nonnull final PrintStream stream, @Nonnull final String level, @Nullable final String message, @Nullable final Throwable th) {
    if (Options.check("verbose")) {
      final String formatted = new Formatter().format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS,%1$tL [%2$5S] %3$s: %4$s", new Date(), level, name, message).toString();
      stream.println(formatted);
      if (th != null) {
        th.printStackTrace(stream);
      }
    }
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("name", name)
            .toString();
  }
}

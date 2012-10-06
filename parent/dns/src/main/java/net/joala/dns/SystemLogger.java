package net.joala.dns;

import org.xbill.DNS.Options;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintStream;
import java.util.Date;
import java.util.Formatter;

import static java.lang.System.*;

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
  private final String name;

  private SystemLogger(final String name) {
    this.name = name;
  }

  public static SystemLogger getLogger(final Class<?> clazz) {
    return new SystemLogger(clazz.getSimpleName());
  }

  public void info(final String msg) {
    println(out, "info", msg, null);
  }

  public void info(final String msg, final Throwable th) {
    println(out, "info", msg, th);
  }

  public void warn(final String msg) {
    println(err, "warn", msg, null);
  }

  public void warn(final String msg, final Throwable th) {
    println(err, "warn", msg, th);
  }

  public void error(final String msg) {
    println(err, "error", msg, null);
  }

  public void error(final String msg, final Throwable th) {
    println(err, "error", msg, th);
  }

  private void println(@Nonnull final PrintStream stream, @Nonnull final String level, @Nullable final String message, @Nullable final Throwable th) {
    if (Options.check("verbose")) {
      final String formatted = new Formatter().format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS,%1$tL [%2$5S] %3$s: %4$s", new Date(), level, name, message).toString();
      stream.println(formatted);
      if (th != null) {
        th.printStackTrace(stream);
      }
    }
  }
}

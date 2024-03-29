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

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.joala.bdd.reference.SelfDescribingReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * This logger is responsible to log JUnit-based methods using BDD-like naming conventions.
 * </p>
 * <p>
 * The naming conventions for pointcuts are methods which match:
 * </p>
 * <ul>
 * <li>{@code execution(* given_*(..))} or</li>
 * <li>{@code execution(* when_*(..))} or</li>
 * <li>{@code execution(* then_*(..))}</li>
 * </ul>
 * <p>
 * To activate this steps logger, ensure that:
 * </p>
 * <ul>
 * <li>your steps come from a Spring Bean (thus from a class injected to your test),</li>
 * <li>if you use an internal class it must not be final and must be public,</li>
 * <li>that you have a context configuration similar to:
 * <pre>{@code
 * <beans xmlns="http://www.springframework.org/schema/beans"
 *        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *        xmlns:context="http://www.springframework.org/schema/context"
 *        xsi:schemaLocation="http://www.springframework.org/schema/beans
 *            http://www.springframework.org/schema/beans/spring-beans.xsd
 *            http://www.springframework.org/schema/context
 *            http://www.springframework.org/schema/context/spring-context.xsd">
 *  <import resource="classpath:META-INF/joala/bdd/bdd-context.xml"/>
 *  <context:component-scan base-package="your.test.package"/>
 * </beans>
 * }</pre>
 * </li>
 * </ul>
 *
 * @see net.joala.bdd
 * @since 6/1/12
 */
@SuppressWarnings({"JavaDoc", "ProhibitedExceptionDeclared", "ProhibitedExceptionThrown"})
@Aspect
public class JUnitAopStepsLogger {
  private static final Logger LOG = LoggerFactory.getLogger(JUnitAopStepsLogger.class);
  /*
   * "Directives" in test method names are character sequences that start with a dollar sign ('$').
   * There are three different types of directives: 2-digit ('x') and 4-digit ('u') hex-encoded escaped
   * Unicode characters, and integers referring to a method argument by its index, starting at zero.
   */
  private static final Pattern DIRECTIVES_PATTERN = Pattern.compile("\\$(x\\p{XDigit}{2}|u\\p{XDigit}{4}|\\d*)");

  /**
   * <p>
   * Adviser to log steps.
   * </p>
   *
   * @param joinPoint where we are
   * @return the result of the step call
   * @throws Throwable in case of any error
   */
  @Around("execution(* given_*(..))||execution(* when_*(..))||execution(* then_*(..))")
  public Object logGivenWhenThen(@Nonnull final ProceedingJoinPoint joinPoint) throws Throwable { // NOSONAR: Need to
    // deal with generic throwables here
    final String stepName = joinPoint.getSignature().getName();
    final Object[] arguments = joinPoint.getArgs();
    final Description stepDescription = describeStep(stepName, arguments);
    final Object result;
    try {
      LOG.info("{}", stepDescription);
      result = joinPoint.proceed();
    } catch (Throwable throwable) { // NOSONAR: Need to deal with generic throwables here
      LOG.info("{} (FAILED)", stepDescription);
      throw throwable;
    }
    return result;
  }

  /**
   * Describe the step, either inserting argument descriptions
   * into the placeholders of the step name or appending them.
   *
   * @param stepName  the step name
   * @param arguments the arguments
   * @return the description
   */
  private Description describeStep(final CharSequence stepName, final Object[] arguments) {
    final StringBuilder text = new StringBuilder();
    int pos = 0;
    boolean placeholderFound = false;
    Matcher matcher = DIRECTIVES_PATTERN.matcher(stepName);
    while (matcher.find()) {
      // append everything up to the current match:
      text.append(stepName, pos, matcher.start());
      // next time, continue after the match:
      pos = matcher.end();
      // the matched directive (without '$'):
      String directive = matcher.group(1);
      if (directive.startsWith("x") || directive.startsWith("u")) {
        // parse 2- or 4-digit hex number and interpret it as Unicode:
        int characterCode = Integer.parseInt(directive.substring(1), 16);
        text.append((char) characterCode);
      } else {
        // integer number => argument reference; empty (just "$") is interpreted as zero:
        int index = directive.isEmpty() ? 0 : Integer.parseInt(directive);
        if (index < arguments.length) {
          final Object argument = arguments[index];
          text.append(describeArgument(argument));
          placeholderFound = true;
        } else {
          // keep invalid argument references as-is:
          text.append(matcher.group());
        }
      }
    }
    // append remaining part of the input string (might be the empty string):
    text.append(stepName, pos, stepName.length());

    final Description stepDescription = new StringDescription();
    // finally, replace all underscores by spaces:
    stepDescription.appendText(text.toString().replace('_', ' ').trim());
    if (!placeholderFound) {
      describeArguments(stepDescription, arguments);
    }
    return stepDescription;
  }

  private Object describeArgument(final Object argument) {
    if (argument == null) {
      return "<null>";
    } else if (argument instanceof String) {
      return "\"" + argument + '"';
    } else if (argument instanceof SelfDescribingReference) {
      return '<' + ((SelfDescribingReference) argument).getName() + '>';
    } else {
      return argument.toString();
    }
  }

  /**
   * <p>
   * Describe the step arguments (most likely references) if they are describable. Ignore them if
   * they are not self describable.
   * </p>
   *
   * @param stepDescription description to add description of arguments to
   * @param args            arguments to describe
   */
  private void describeArguments(@Nonnull final Description stepDescription, @Nonnull final Object[] args) {
    final List<Object> argsList = Arrays.asList(args);
    final Iterable<Object> descArgs = Iterables.filter(argsList, new IsSelfDescribing());
    final boolean hasDescribableArgs = !Iterables.isEmpty(descArgs);
    if (hasDescribableArgs) {
      stepDescription.appendText(" (");
      final Iterable<String> transformed = Iterables.transform(descArgs, new DescribeArgument());
      stepDescription.appendText(Joiner.on(',').join(transformed));
      stepDescription.appendText(")");
    }
  }

  /**
   * <p>
   * Filter to locate self describing arguments.
   * </p>
   */
  private static class IsSelfDescribing implements Predicate<Object> {
    @Override
    public boolean apply(@Nullable final Object input) {
      return input instanceof SelfDescribing || input instanceof SelfDescribing[];
    }
  }

  private static class DescribeArgument implements Function<Object, String> {
    @Override
    public String apply(@Nullable final Object input) {
      final Description description = new StringDescription();
      if (input instanceof SelfDescribing) {
        description.appendDescriptionOf((SelfDescribing) input);
      } else if (input instanceof SelfDescribing[]) {
        description.appendList("{", ",", "}", Arrays.asList((SelfDescribing[]) input));
      }
      return description.toString();
    }
  }
}

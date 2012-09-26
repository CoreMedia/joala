/*
 * Copyright 2012 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.joala.bdd.aop;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
 * <pre>
 * {@code <?xml version="1.0" encoding="UTF-8"?>
 *   <beans xmlns="http://www.springframework.org/schema/beans"
 *          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *          xmlns:aop="http://www.springframework.org/schema/aop"
 *          xmlns:context="http://www.springframework.org/schema/context"
 *          xsi:schemaLocation="http://www.springframework.org/schema/beans
 *             http://www.springframework.org/schema/beans/spring-beans.xsd
 *             http://www.springframework.org/schema/aop
 *             http://www.springframework.org/schema/aop/spring-aop.xsd
 *             http://www.springframework.org/schema/context
 *             http://www.springframework.org/schema/context/spring-context.xsd">
 *       <aop:aspectj-autoproxy/>
 *       <context:component-scan base-package="com.coremedia.uitesting.junit.bdd.aop"/>
 *     </beans>
 *     }
 *   </pre>
 * </li>
 * </ul>
 *
 * @since 6/1/12
 */
@SuppressWarnings("JavaDoc")
@Aspect
@Component
public class JUnitAopStepsLogger {
  private static final Logger LOG = LoggerFactory.getLogger(JUnitAopStepsLogger.class);

  @Around("execution(* given_*(..))||execution(* when_*(..))||execution(* then_*(..))")
  public Object logGivenWhenThen(final ProceedingJoinPoint joinPoint) throws Throwable {
    final Description stepDescription = new StringDescription();
    final String stepName = joinPoint.getSignature().getName();
    stepDescription.appendText(stepName.replace('_', ' ').trim());
    describeArguments(stepDescription, joinPoint.getArgs());
    final Object result;
    try {
      LOG.info("{}", stepDescription);
      result = joinPoint.proceed();
    } catch (Throwable throwable) {
      LOG.info("{} (FAILED)", stepDescription);
      throw throwable;
    }
    return result;
  }

  private void describeArguments(final Description stepDescription, final Object[] args) {
    final List<Object> argsList = Arrays.asList(args);
    final Collection<Object> describableArgs = Collections2.filter(argsList, new Predicate<Object>() {
      @Override
      public boolean apply(final Object input) {
        return (input instanceof SelfDescribing);
      }
    });
    if (describableArgs.size() > 0) {
      stepDescription.appendText(" (");
    }
    for (Iterator<Object> argumentIterator = describableArgs.iterator(); argumentIterator.hasNext(); ) {
      final SelfDescribing describableArg = (SelfDescribing) argumentIterator.next();
      describableArg.describeTo(stepDescription);
      if (argumentIterator.hasNext()) {
        stepDescription.appendText(", ");
      }
    }
    if (describableArgs.size() > 0) {
      stepDescription.appendText(")");
    }
  }
}

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

package net.joala.testlet;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import net.joala.core.reflection.SetAccessibleAction;
import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.security.AccessController.doPrivileged;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Tests some requirements for the {@code toString()} method. See the test methods
 * for the tested requirements.
 * </p>
 * <p>
 * Failures of the ToStringTestlets are most of the time results of refactorings
 * to variable names so that the toString output does not refer to a variable name
 * anylonger.
 * </p>
 *
 * @since 10/4/12
 */
@Ignore("Don't execute Testlets in IDE and alike.")
@SuppressWarnings("JUnitTestClassNamingConvention")
public class ToStringTestlet<T> extends AbstractTestlet<T> {
  /**
   * Logging instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(ToStringTestlet.class);

  /**
   * Fields collected from the given class.
   */
  @Nonnull
  private Class<? super T> fieldsFromClass;
  private boolean constantFields;
  private boolean staticFields;
  private boolean transientFields = true;
  private boolean enumFields = true;
  private Pattern excludeNamePattern;
  private Pattern excludeTypeNamePattern;
  private boolean ignoreClassname;

  /**
   * <p>
   * Constructor with the testling.
   * </p>
   *
   * @param testling testling to test the {@code toString()} method of
   */
  @SuppressWarnings("unchecked")
  private ToStringTestlet(@Nonnull final T testling) {
    super(testling);
    fieldsFromClass = (Class<? super T>) testling.getClass();
  }

  @Test
  public void toString_should_contain_classname() {
    if (!ignoreClassname) {
      assertThat("toString should contain classname", getTestling().toString(), containsString(getTestling().getClass().getSimpleName()));
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void toString_should_contain_all_fields_and_their_values() throws IllegalAccessException {
    final Field[] declaredFields = getExpectedFields();
    LOG.debug("Validating {} fields of {}.", declaredFields.length, fieldsFromClass);
    final Collection<Matcher<? super String>> fieldMatchers = new ArrayList<Matcher<? super String>>(declaredFields.length * 2);
    for (final Field declaredField : declaredFields) {
      if (!Modifier.isStatic(declaredField.getModifiers())) {
        doPrivileged(new SetAccessibleAction(declaredField));
        final String fieldName = declaredField.getName();
        fieldMatchers.add(containsString(fieldName));
        fieldMatchers.add(containsString(String.valueOf(declaredField.get(getTestling()))));
        LOG.debug("Added validator for field {} of class {}.", fieldName, fieldsFromClass);
      }
    }
    final Matcher<String> matcher = allOf(fieldMatchers);
    assertThat("toString should contain all fields and their values", getTestling().toString(), matcher);
  }

  @Nonnull
  private Field[] getExpectedFields() {
    final List<Field> fields = Arrays.asList(fieldsFromClass.getDeclaredFields());
    final Collection<Field> filteredFields = Collections2.filter(fields, new FieldPredicate());
    return filteredFields.toArray(new Field[filteredFields.size()]);
  }

  /**
   * <p>
   * Include all static final fields. Excluded by default.
   * </p>
   *
   * @return self reference
   */
  @Nonnull
  public ToStringTestlet<T> includeConstantFields() {
    constantFields = true;
    return this;
  }

  /**
   * <p>
   * Especially if you use mocking frameworks the class name might be modified and thus
   * be untestable. In these cases it is recommended to ignore the classname test.
   * </p>
   *
   * @return self reference
   */
  public ToStringTestlet<T> ignoreClassname() {
    ignoreClassname = true;
    return this;
  }

  /**
   * <p>
   * Include all static fields. Excluded by default.
   * </p>
   *
   * @return self reference
   */
  @Nonnull
  public ToStringTestlet<T> includeStaticFields() {
    staticFields = true;
    return this;
  }

  /**
   * <p>
   * Exclude all transient fields. Included by default.
   * </p>
   *
   * @return self reference
   */
  @Nonnull
  public ToStringTestlet<T> excludeTransientFields() {
    transientFields = false;
    return this;
  }

  /**
   * <p>
   * Exclude all fields containing enum values. Included by default.
   * </p>
   *
   * @return self reference
   */
  @Nonnull
  public ToStringTestlet<T> excludeEnumFields() {
    enumFields = false;
    return this;
  }

  /**
   * <p>
   * Exclude all fields whose name matches the given pattern.
   * </p>
   *
   * @param excludeNamePattern pattern which matches field names to exclude
   * @return self reference
   */
  @Nonnull
  public ToStringTestlet<T> excludeFieldNames(@Nullable final Pattern excludeNamePattern) {
    this.excludeNamePattern = excludeNamePattern;
    return this;
  }

  /**
   * <p>
   * Exclude all fields whose type names matches the given pattern.
   * </p>
   *
   * @param excludeTypeNamePattern pattern which matches field type names to exclude
   * @return self reference
   */
  @Nonnull
  public ToStringTestlet<T> excludeFieldTypeNames(@Nullable final Pattern excludeTypeNamePattern) {
    this.excludeTypeNamePattern = excludeTypeNamePattern;
    return this;
  }

  /**
   * <p>
   * Don't try to access fields from testling but instead take fields from the given class.
   * </p>
   *
   * @param fromClass class to take the declared fields from
   * @return self reference
   */
  @Nonnull
  public ToStringTestlet<T> fieldsFromClass(@Nonnull final Class<? super T> fromClass) {
    fieldsFromClass = checkNotNull(fromClass, "fromClass must not be null.");
    return this;
  }

  @SuppressWarnings("ProhibitedExceptionDeclared")
  public static <T> ToStringTestlet<T> toStringTestlet(@Nonnull final T testling) throws Throwable { // NOSONAR: Throwable comes from JUnit API
    return new ToStringTestlet<T>(testling);
  }

  private class FieldPredicate implements Predicate<Field> {
    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean apply(final Field input) {
      final int modifiers = input.getModifiers();
      if (!constantFields && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
        return false;
      }
      if (!staticFields && Modifier.isStatic(modifiers)) {
        return false;
      }
      if (!transientFields && Modifier.isTransient(modifiers)) {
        return false;
      }
      if (!enumFields && input.isEnumConstant()) {
        return false;
      }
      if (excludeNamePattern != null && excludeNamePattern.matcher(input.getName()).matches()) {
        return false;
      }
      if (excludeTypeNamePattern != null && excludeTypeNamePattern.matcher(input.getType().getName()).matches()) {
        return false;
      }
      return true;
    }
  }
}

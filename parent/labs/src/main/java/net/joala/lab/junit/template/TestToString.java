package net.joala.lab.junit.template;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.joala.lab.junit.template.AssertTemplateTest.assertNoFailures;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Tests some requirements for the {@code toString()} method.
 * Create instance with factory method {@link #testToString(Object)}.
 * </p>
 *
 * @since 10/4/12
 */
// Class is abstract in order to prevent that running all tests from within your IDE
// also starts this test.
@SuppressWarnings("JUnitTestClassNamingConvention")
public abstract class TestToString {
  private static final Logger LOG = LoggerFactory.getLogger(TestToString.class);

  private final Object objectUnderTest;
  private final Class<?> fieldDeclaringClass;

  private TestToString(@Nonnull final Object objectUnderTest) {
    this(objectUnderTest, objectUnderTest.getClass());
  }

  private TestToString(@Nonnull final Object objectUnderTest, @Nonnull final Class<?> fieldDeclaringClass) {
    this.objectUnderTest = objectUnderTest;
    this.fieldDeclaringClass = fieldDeclaringClass;
  }

  @Test
  public void toString_should_contain_classname() {
    assertThat("toString should contain classname", objectUnderTest.toString(), containsString(objectUnderTest.getClass().getSimpleName()));
  }

  @Test
  public void toString_should_contain_all_fields_and_their_values() throws IllegalAccessException {
    final Field[] declaredFields = fieldDeclaringClass.getDeclaredFields();
    LOG.debug("Validating {} fields of {}.", declaredFields.length, fieldDeclaringClass);
    final Collection<Matcher<? super String>> fieldMatchers = new ArrayList<Matcher<? super String>>(declaredFields.length * 2);
    for (final Field declaredField : declaredFields) {
      if (!Modifier.isStatic(declaredField.getModifiers())) {
        declaredField.setAccessible(true);
        final String fieldName = declaredField.getName();
        fieldMatchers.add(containsString(fieldName));
        fieldMatchers.add(containsString(String.valueOf(declaredField.get(objectUnderTest))));
        LOG.debug("Added validator for field {} of class {}.", fieldName, fieldDeclaringClass);
      }
    }
    assertThat("toString should contain all fields and their values", objectUnderTest.toString(), allOf(fieldMatchers));
  }

  public static void testToString(@Nonnull final Object objectUnderTest) throws Throwable {
    checkNotNull(objectUnderTest, "Object under Test must not be null.");
    //noinspection AnonymousInnerClass
    assertNoFailures(new TestToString(objectUnderTest){});
  }

  public static void testToString(@Nonnull final Object objectUnderTest, @Nonnull final Class<?> fieldDeclaringClass) throws Throwable {
    checkNotNull(objectUnderTest, "Object under Test must not be null.");
    checkNotNull(fieldDeclaringClass, "Field declaring class must not be null.");
    //noinspection AnonymousInnerClass
    assertNoFailures(new TestToString(objectUnderTest, fieldDeclaringClass){});
  }

}

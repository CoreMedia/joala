package net.joala.lab.junit.template;

import com.google.common.base.Objects;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static net.joala.lab.junit.template.AssertTemplateTest.assertNoFailures;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;

/**
 * <p>
 * Test to verify if provided class meets utility class requirements.
 * This is especially useful to get code coverage for required private
 * constructors.
 * </p>
 *
 * @since 10/5/12
 */
// Class is abstract in order to prevent that running all tests from within your IDE
// also starts this test.
public abstract class TestUtilityClass {
  private final Class<?> utilityClassUnderTest;

  private TestUtilityClass(final Class<?> utilityClassUnderTest) {
    this.utilityClassUnderTest = utilityClassUnderTest;
  }

  @Test
  public void utilityClass_should_have_private_noarg_constructor() {
    try {
      final Constructor<?> declaredConstructor = utilityClassUnderTest.getDeclaredConstructor();
      final int modifiers = declaredConstructor.getModifiers();
      assertTrue("Noarg constructor must be private.", Modifier.isPrivate(modifiers));
    } catch (NoSuchMethodException ignored) {
      fail(format("Class %s has no noarg constructor.", utilityClassUnderTest));
    }
  }

  @Test
  public void utilityClass_should_be_final() {
    assertTrue("Utility Class should be final.", Modifier.isFinal(utilityClassUnderTest.getModifiers()));
  }

  @Test
  public void utilityClass_noarg_constructor_should_pass() throws InvocationTargetException, IllegalAccessException, InstantiationException {
    try {
      final Constructor<?> declaredConstructor = utilityClassUnderTest.getDeclaredConstructor();
      declaredConstructor.setAccessible(true);
      try {
        declaredConstructor.newInstance();
      } catch (Exception e) {
        assertThat("Should have called noarg constructor without failure.", e, nullValue());
      }
    } catch (NoSuchMethodException e) {
      assumeThat(e, nullValue());
    }
  }

  public static void testUtilityClass(@Nonnull final Class<?> utilityClassUnderTest) throws Throwable {
    checkNotNull(utilityClassUnderTest, "Class under test must not be null.");
    //noinspection AnonymousInnerClass
    assertNoFailures(new TestUtilityClass(utilityClassUnderTest) {
    });
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("utilityClassUnderTest", utilityClassUnderTest)
            .toString();
  }
}

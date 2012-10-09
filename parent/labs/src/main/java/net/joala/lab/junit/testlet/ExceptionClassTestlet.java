package net.joala.lab.junit.testlet;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.concurrent.Callable;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isFinal;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;

/**
 * <p>
 * Test an exception and its constructors. The testlet is prepared to
 * deal with standard exception constructors. It will scan for them,
 * feed them with information and see if the information will be
 * available after construction.
 * </p>
 *
 * @since 10/5/12
 */
@SuppressWarnings("JUnitTestClassNamingConvention")
@Ignore("Don't execute Testlets in IDE and alike.")
public class ExceptionClassTestlet<T extends Throwable> extends AbstractTestlet<Class<? super T>> {
  @Rule
  public ErrorCollector collector = new ErrorCollector();
  /**
   * <p>
   * Builds the testlet with the specified class under test.
   * </p>
   *
   * @param testling artifact under test
   */
  protected ExceptionClassTestlet(@Nonnull final Class<? super T> testling) {
    super(testling);
  }

  @Test(expected = NoSuchMethodError.class) // ignore, we are fine
  public void noarg_constructor_should_meet_requirements() throws Exception {
    final Constructor<? super T> constructor;
    constructor = getTestling().getDeclaredConstructor();
    collector.checkThat(constructor, net.joala.matcher.reflect.isAccessible());
    commonConstructorAssertions(constructor);
    collector.checkSucceeds(new Callable<Object>() {
      @Override
      public Object call() throws Exception {
        return constructor.newInstance();
      }
    });
    collector.
    try {
      constructor.newInstance()
    } catch (Exception e) {
      fail("");
    } catch (RuntimeException e) {
    }
  }

  private void commonConstructorAssertions(final Constructor<? super T> constructor) {
    assertTrue("Constructor should be accessible.", constructor.isAccessible());
    assertTrue("Constructor should be public.", Modifier.isPublic(constructor.getModifiers()));
  }

  @Test
  public void utilityClass_should_have_private_noarg_constructor() {
    try {
      final Constructor<?> declaredConstructor = getTestling().getDeclaredConstructor();
      final int modifiers = declaredConstructor.getModifiers();
      assertTrue("Noarg constructor must be private.", Modifier.isPrivate(modifiers));
    } catch (NoSuchMethodException ignored) {
      fail(format("Class %s has no noarg constructor.", getTestling()));
    }
  }

  @Test
  public void utilityClass_should_be_final() {
    assertTrue("Utility Class should be final.", isFinal(getTestling().getModifiers()));
  }

  @Test
  public void utilityClass_noarg_constructor_should_pass() throws InvocationTargetException, IllegalAccessException, InstantiationException {
    try {
      final Constructor<?> declaredConstructor = getTestling().getDeclaredConstructor();
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

  @SuppressWarnings("ProhibitedExceptionDeclared")
  public static ExceptionClassTestlet utilityClassTestlet(@Nonnull final Class<?> utilityClassUnderTest) throws Throwable { // NOSONAR: Throwable inherited from JUnit
    return new ExceptionClassTestlet(utilityClassUnderTest);
  }

}

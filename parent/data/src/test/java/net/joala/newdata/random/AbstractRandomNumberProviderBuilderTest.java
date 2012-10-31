package net.joala.newdata.random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.Random;

import static net.joala.newdata.random.PoorMansReference.ref;
import static net.joala.testlet.ToStringTestlet.toStringTestlet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * <p>
 * Tests {@link AbstractRandomNumberProviderBuilder}.
 * </p>
 *
 * @since 10/31/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
@RunWith(PowerMockRunner.class)
public class AbstractRandomNumberProviderBuilderTest {

  // ========== S C E N A R I O S ========== //

  @Test
  public void use_given_randomProvider() throws Exception {
    final PoorMansReference<Provider<Random>> P = ref();
    final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> B = ref();

    given_random_provider_P(P);
    when_the_builder_B_is_created_with_random_provider_P(B, P);
    then_a_new_provider_is_built_by_builder_B_with_random_provider_P(B, P);
  }

  @Test
  public void use_given_null_randomProvider() throws Exception {
    final PoorMansReference<Provider<Random>> P = ref();
    final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> B = ref();

    given_null_as_random_provider_P(P);
    when_the_builder_B_is_created_with_random_provider_P(B, P);
    then_a_new_provider_is_built_by_builder_B_with_random_provider_P(B, P);
  }

  @Test
  public void provided_min_value_is_used_on_build() throws Exception {
    final PoorMansReference<Long> V = ref();
    final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> B = ref();

    given_a_minimum_value(V);
    when_builder_B_is_configured_to_use_minimum_value_V(B, V);
    then_a_new_provider_is_built_by_builder_B_with_minimum_value_V(B, V);
  }

  @Test
  public void provided_max_value_is_used_on_build() throws Exception {
    final PoorMansReference<Long> V = ref();
    final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> B = ref();

    given_a_maximum_value(V);
    when_builder_B_is_configured_to_use_maximum_value_V(B, V);
    then_a_new_provider_is_built_by_builder_B_with_maximum_value_V(B, V);
  }

  @Test
  public void builder_prohibits_setting_minimum_value_twice() throws Exception {
    final PoorMansReference<Throwable> expectedException = ref();
    final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> B = ref();

    given_builder_B_is_configured_to_use_some_minimum_value(B);
    when_I_try_to_configure_another_minimum_value_for_builder_B(B, expectedException);
    then_an_exception_is_raised(expectedException);
  }

  @Test
  public void builder_prohibits_setting_maximum_value_twice() throws Exception {
    final PoorMansReference<Throwable> expectedException = ref();
    final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> B = ref();

    given_builder_B_is_configured_to_use_some_maximum_value(B);
    when_I_try_to_configure_another_maximum_value_for_builder_B(B, expectedException);
    then_an_exception_is_raised(expectedException);
  }

  @Test
  public void builder_prohibits_minimum_reconfiguration_after_build() throws Exception {
    final PoorMansReference<Throwable> expectedException = ref();
    final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> B = ref();

    // This is a good example for reusing steps.

    // New
    given_builder_B_already_built_a_number_provider(B);
    // Re-use
    when_I_try_to_configure_another_minimum_value_for_builder_B(B, expectedException);
    // Re-use
    then_an_exception_is_raised(expectedException);
  }

  @Test
  public void builder_prohibits_maximum_reconfiguration_after_build() throws Exception {
    final PoorMansReference<Throwable> expectedException = ref();
    final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> B = ref();

    // Complete re-usage: This test got built just by providing some other steps which
    // were already implemented.
    given_builder_B_already_built_a_number_provider(B);
    // Example for "silent" references actually not mentioned in the step
    // description but required for the next step. This cannot be done with frameworks
    // like JBehave etc.
    when_I_try_to_configure_another_maximum_value_for_builder_B(B, expectedException);
    then_an_exception_is_raised(expectedException);
  }

  @Test
  public void fluent_minimum_method_returns_self_reference() throws Exception {
    final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> B = ref();
    final PoorMansReference<Object> returnValue = ref();
    given_a_builder_B(B);
    when_I_set_a_minimum_value_for_builder_B(B, returnValue);
    then_the_method_returns_a_self_reference_to_builder_B(B, returnValue);
  }

  @Test
  public void fluent_maximum_method_returns_self_reference() throws Exception {
    final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> B = ref();
    final PoorMansReference<Object> returnValue = ref();
    given_a_builder_B(B);
    when_I_set_a_maximum_value_for_builder_B(B, returnValue);
    then_the_method_returns_a_self_reference_to_builder_B(B, returnValue);
  }

  @Test
  public void toString_should_meet_requirements() throws Throwable {
    PowerMockito.doCallRealMethod().when(randomNumberProviderBuilder, "toString");
    toStringTestlet(randomNumberProviderBuilder)
            .ignoreClassname()
            .fieldsFromClass(AbstractRandomNumberProviderBuilder.class)
            .run();
  }

  // ========== S T E P S ========== //

  /**
   * Implement abstract class by mocking.
   *
   * @see <a href="http://stackoverflow.com/questions/1087339/using-mockito-to-test-abstract-classes">Stackoverflow:
   *      Using Mockito to test abstract classes</a>
   */
  @Mock(answer = Answers.CALLS_REAL_METHODS)
  private AbstractRandomNumberProviderBuilder<Long> randomNumberProviderBuilder;

  @Mock
  private Provider<Random> randomProvider;

  @Mock
  private Provider<Long> numberProvider;

  @Captor
  private ArgumentCaptor<Long> minValueCaptor;
  @Captor
  private ArgumentCaptor<Long> maxValueCaptor;
  @Captor
  private ArgumentCaptor<Provider<Random>> randomProviderCaptor;

  private static final Long SOME_MINUMUM_VALUE = 23L;
  private static final Long SOME_MAXIMUM_VALUE = SOME_MINUMUM_VALUE * 2;

  // G I V E N

  private void given_random_provider_P(final PoorMansReference<Provider<Random>> p) {
    p.set(randomProvider);
  }

  private void given_null_as_random_provider_P(final PoorMansReference<Provider<Random>> p) {
    p.set(null);
  }

  private void given_a_minimum_value(final PoorMansReference<Long> v) {
    v.set(SOME_MINUMUM_VALUE);
  }

  private void given_a_maximum_value(final PoorMansReference<Long> v) {
    v.set(SOME_MAXIMUM_VALUE);
  }

  private void given_builder_B_is_configured_to_use_some_minimum_value(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b) {
    b.set(new MockBuilder(randomProvider, numberProvider));
    b.get().min(SOME_MINUMUM_VALUE);
  }

  private void given_builder_B_is_configured_to_use_some_maximum_value(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b) {
    b.set(new MockBuilder(randomProvider, numberProvider));
    b.get().min(SOME_MAXIMUM_VALUE);
  }

  private void given_builder_B_already_built_a_number_provider(final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b) {
    b.set(new MockBuilder(randomProvider, numberProvider));
    b.get().build();
  }

  private void given_a_builder_B(final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b) {
    b.set(new MockBuilder(randomProvider, numberProvider));
  }

  // W H E N

  private void when_the_builder_B_is_created_with_random_provider_P(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b,
          final PoorMansReference<Provider<Random>> p) {
    b.set(new MockBuilder(randomProvider, numberProvider));
    p.set(randomProvider);
  }

  private void when_builder_B_is_configured_to_use_minimum_value_V(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b,
          final PoorMansReference<Long> v) {
    b.set(new MockBuilder(randomProvider, numberProvider));
    b.get().min(v.get());
  }

  private void when_builder_B_is_configured_to_use_maximum_value_V(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b,
          final PoorMansReference<Long> v) {
    b.set(new MockBuilder(randomProvider, numberProvider));
    b.get().max(v.get());
  }

  private void when_I_try_to_configure_another_minimum_value_for_builder_B(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b,
          final PoorMansReference<Throwable> expectedException) {
    try {
      b.get().min(SOME_MAXIMUM_VALUE);
      expectedException.set(null);
    } catch (Throwable e) {
      expectedException.set(e);
    }
  }

  private void when_I_try_to_configure_another_maximum_value_for_builder_B(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b,
          final PoorMansReference<Throwable> expectedException) {
    try {
      b.get().min(SOME_MINUMUM_VALUE);
      expectedException.set(null);
    } catch (Throwable e) {
      expectedException.set(e);
    }
  }

  private void when_I_set_a_minimum_value_for_builder_B(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b,
          final PoorMansReference<Object> returnValue) {
    returnValue.set(b.get().min(SOME_MINUMUM_VALUE));
  }

  private void when_I_set_a_maximum_value_for_builder_B(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b,
          final PoorMansReference<Object> returnValue) {
    returnValue.set(b.get().min(SOME_MAXIMUM_VALUE));
  }

  // T H E N

  private void then_a_new_provider_is_built_by_builder_B_with_random_provider_P(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b,
          final PoorMansReference<Provider<Random>> p) {
    final AbstractRandomNumberProviderBuilder<Long> spiedBuilder = PowerMockito.spy(b.get());
    when(spiedBuilder.newProvider(
            Matchers.anyLong(),
            Matchers.anyLong(),
            randomProviderCaptor.capture()))
            .thenCallRealMethod();
    spiedBuilder.build();
    assertSame("Random Provider should have been used as defined on construction.", p.get(), randomProviderCaptor.getValue());
  }

  private void then_a_new_provider_is_built_by_builder_B_with_minimum_value_V(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b,
          final PoorMansReference<Long> v) {
    final AbstractRandomNumberProviderBuilder<Long> spiedBuilder = PowerMockito.spy(b.get());
    when(spiedBuilder.newProvider(
            minValueCaptor.capture(),
            Matchers.anyLong(),
            Matchers.any(RandomProvider.class)))
            .thenCallRealMethod();
    spiedBuilder.build();
    assertEquals("Minimum value should be used as specified.", v.get(), minValueCaptor.getValue());
  }

  private void then_a_new_provider_is_built_by_builder_B_with_maximum_value_V(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b,
          final PoorMansReference<Long> v) {
    final AbstractRandomNumberProviderBuilder<Long> spiedBuilder = PowerMockito.spy(b.get());
    when(spiedBuilder.newProvider(
            Matchers.anyLong(),
            maxValueCaptor.capture(),
            Matchers.any(RandomProvider.class)))
            .thenCallRealMethod();
    spiedBuilder.build();
    assertEquals("Maximum value should be used as specified.", v.get(), maxValueCaptor.getValue());
  }

  private void then_an_exception_is_raised(final PoorMansReference<Throwable> expectedException) {
    assertNotNull("An exception should have been raised.", expectedException.get());
  }

  private void then_the_method_returns_a_self_reference_to_builder_B(
          final PoorMansReference<AbstractRandomNumberProviderBuilder<Long>> b,
          final PoorMansReference<Object> returnValue) {
    assertSame("Self-reference should have been returned.", b.get(), returnValue.get());
  }

  // ========== S U P P O R T ========== //

  private static class MockBuilder extends AbstractRandomNumberProviderBuilder<Long> {
    @Nonnull
    private final Provider<Long> numberProvider;
    private final boolean validRange;

    private MockBuilder(
            @Nullable final Provider<Random> randomProvider,
            @Nonnull final Provider<Long> numberProvider) {
      this(randomProvider, numberProvider, true);
    }

    private MockBuilder(
            @Nullable final Provider<Random> randomProvider,
            @Nonnull final Provider<Long> numberProvider,
            final boolean validRange) {
      super(randomProvider);
      this.numberProvider = numberProvider;
      this.validRange = validRange;
    }

    @Nonnull
    @Override
    protected Provider<Long> newProvider(@Nullable final Long minValue, @Nullable final Long maxValue, @Nullable final Provider<Random> randomProvider) {
      return numberProvider;
    }
  }
}

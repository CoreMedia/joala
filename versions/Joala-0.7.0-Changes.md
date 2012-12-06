# Joala Release Notes, v0.7.0

## Bugs

* [joala-27][]: **Vararg references are not logged**

## Changes

* [joala-29][]: **Switched to GPL license**
* [joala-30][]: **Introduce module `joala-expressions-library`**
    Introduced new module. Removed `UriStatusCodeExpression` from module `joala-expressions` and moved and repackaged
    it in `joala-expressions-library`. New FQN is `net.joala.expressions.library.net.UriStatusCodeExpression`.

## Features

* [joala-23][]: From Labs: **Introducing Testlets**
    Testlets are now an official module. They contain sets of prepared tests you can easily add to your own tests.
    For example you have tests for the `toString` method if it matches certain requirements. Having this it is a
    piece of cake to reach a higher test coverage with just a little effort.
* [joala-32][]: **Log steps parameters using placeholders in step names**
    In order to provide a better reusage of test steps and to enhance the logging experience for tests
    written with Gherkin language step parameters can now be referenced in the step description:

    ```java
_.given_this_is_a_step_with_an_inserted_argument_$0(42);
    ```

## Labs

Issues mentioned in here refer to experimental features you should only use with care as they are subject
to change.

* [joala-22][]: **Introduce Builder for Parameterized Parameters**
    Building parametrized tests in JUnit is sometimes pain. You forget to fill additional arguments to the
    objects array and such. The builder introduced makes it much easier and also provides reasonable error
    reports for misconfigured parameters. Example:

    ```java
public TimeFormatTest(long amount, TimeUnit timeUnit, String expectedTimePattern) {
  ...
}
@Parameterized.Parameters
public static Collection<Object[]> data() {
  return defaultParametersBuilder(TimeFormatTest.class)
          .add(TimeUnit.SECONDS.toMillis(TimeFormat.TIMEUNIT_LIMIT), TimeUnit.MILLISECONDS, "^2\\ss$")
          .add(TimeUnit.MINUTES.toMillis(TimeFormat.TIMEUNIT_LIMIT) - 1, TimeUnit.MILLISECONDS, "^119\\ss$")
          ...
          .add(0L, TimeUnit.HOURS, "^0\\sh$")
          .add(0L, TimeUnit.DAYS, "^0\\sd$")
          .build();
  }
    ```

* [joala-31][]: **Make joala-labs Multimodule**
    In order to provide better incubation behavior for labs features they are now seperated into different
    modules. Mind that if you depended on joala-labs before you need to update to the matching submodule.

## Deprecations

* [joala-40][]: **Remove deprecated package `net.joala.condition.timing`**
    Since 0.5.0 the classes moved to `net.joala.time`. Deprecations got deleted in this release.
* [joala-17][]: **Deprecations: Remove Expression deprecations**
    Expressions moved from conditions to a new module expressions in 0.5.0. The deprecated classes now got deleted.

[joala-30]: <https://github.com/CoreMedia/joala/issues/30> "#30: Introduce module joala-expressions-library"
[joala-40]: <https://github.com/CoreMedia/joala/issues/40> "#40: Deprecations: Remove net.joala.condition.timing"
[joala-17]: <https://github.com/CoreMedia/joala/issues/17> "#17: Deprecations: Remove Expression deprecations"
[joala-29]: <https://github.com/CoreMedia/joala/issues/29> "#29: Move to GPL for Licensing"
[joala-27]: <https://github.com/CoreMedia/joala/issues/27> "#27: Vararg references are not logged"
[joala-23]: <https://github.com/CoreMedia/joala/issues/23> "#23: Matured joala-labs part: Move testlets to official module"
[joala-22]: <https://github.com/CoreMedia/joala/issues/22> "#22: Introduce Builder for Parameterized Parameters"
[joala-31]: <https://github.com/CoreMedia/joala/issues/31> "#31: Make joala-labs Multimodule"
[joala-32]: <https://github.com/CoreMedia/joala/issues/32> "#32: Log steps parameters using placeholders in step names"

# Joala Test-Categories

[JUnit][] supports [Categories][] to apply to tests in order to allow a tester to select
the test to run given those categories.

## Using Categories for Test Suites

In order to create a test-suite of fast running JUnit tests create a Suite-file which looks
like this:

```java
@RunWith(Categories.class)
@Categories.IncludeCategory(Unit.class)
@Categories.ExcludeCategory({})
@Suite.SuiteClasses({AnotherFirefoxTest.class, FirefoxTest.class})
public class PocTestSuite {
  public PocTestSuite() {
    Description.createSuiteDescription(PocTestSuite.class);
  }
}
```

<!-- Links -->

[JUnit]: <http://www.junit.org/> ""
[Categories]: <http://kentbeck.github.com/junit/javadoc/latest/org/junit/experimental/categories/Categories.html> "JUnit Categories"

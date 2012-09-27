package net.joala.bdd;
/**
 * <p>
 *   Support to write JUnit tests in BDD style.
 * </p>
 * <p>
 *   <strong>Intention</strong>
 * </p>
 * <p>
 *   You might know this problem: 10 years ago some developer wrote a sophisticated test to test some implemented
 *   feature. 10 years ago this person knew the feature very well and wrote a test with many preparation steps and
 *   perhaps with some assertions. Guess - this person might have been you.
 * </p>
 * <p>
 *   Now, 10 years later, your product got refactored many times. The once so obvious feature is hidden somewhere or
 *   you just forgot about it. But suddenly the test fails. It might be that you just removed the feature - perhaps by
 *   intention to get rid of dead unused code. But could you tell this from the test which now fails? If you are lucky.
 *   Practice tells another story. Sometimes you glare at the test and don't understand a word. What was it meant to do?
 *   What did it test?
 * </p>
 * <p>
 *   Joala BDD provides a solution for this using a well known language template to write tests and without the need
 *   to learn any other technology. You just need to know Java - and perhaps need a slight idea about Spring.
 * </p>
 * <p>
 *   <strong>Approach</strong>
 * </p>
 * <p>
 *   BDD is the acronym for Behavior Driven Development. You describe a wanted behavior. And if you do it in a formal
 *   language, the description can be used for automated tests. This is where tools like
 *   <a href="http://cukes.info/">Cucumber</a> or <a href="http://jbehave.org/">JBehave</a> started.
 *   The formal language used is called Gherkin.
 * </p>
 * <p>
 *   If you start with 3rd party tools you will need to find the one which is best supported by your IDE. Otherwise
 *   you will get stuck like many others did with a heap of unmaintainable stories you cannot refactor.
 * </p>
 * <p>
 *   Joala BDD stays in the world of Java - and such you have full refactoring support in your IDE and can use
 *   many static code analysis tools like for example locating unused step definitions.
 * </p>
 * <p>
 *   <strong>References</strong>
 * </p>
 * <p>
 *   If you describe a story step by step you typically need to carry information between those steps. For example
 *   you create a HTML document in one preparation step (<em>Given</em>), fill in some unicode text into that document
 *   in the action step (<em>When</em>) and eventually you assert that the text is correctly rendered in a browser
 *   (<em>Then</em>).
 * </p>
 * <p>
 *   Using methods with return values will degrade the readability of your tests and you will stumble across
 *   preparation steps preparing multiple test artifacts. References are the solution. They are used as container
 *   for the prepared objects and have advantages like providing properties for later assertions. See more in
 *   the next chapter.
 * </p>
 * <p>
 *   <strong>Concepts/Recommendations</strong>
 * </p>
 * <p>
 *   The following describes a set of concepts and/or recommendations. You don't need to stick to all of them
 *   but the following approach promises to work best. Mind that most steps are only done once in case you use
 *   a base class for your tests (which is recommended). Thus to create the real story files later on is most
 *   of the time a piece of cake.
 * </p>
 * <ol>
 *   <li><em>Create the Story Test:</em> Create a test class named {@code StoryMyFeatureTitleTest}.</li>
 *   <li><em>Add Test Watcher:</em> Add a JUnit rule in order to provide logging for Story class and
 *   scenario test methods. It is good practice to do this configuration in a base class all story classes
 *   inherit from:
 *   <pre>{@code
 *   &#64;Rule
 *   public final TestWatcher testWatcher = new JUnitScenarioWatcher();
 *   }</pre>
 *   </li>
 *   <li><em>Use JUnit Runner from Spring:</em> The logging approach uses Spring AOP to log the different steps.
 *   As first approach you need to ensure that Spring is used to run the tests by anotating the test class (or its
 *   base class) with the following:
 *   <pre>{@code
 *   &#64;RunWith(SpringJUnit4ClassRunner.class)
 *   &#64;ContextConfiguration
 *   }</pre>
 *   </li>
 *   <li><em>Prepare Test Context:</em> The context configuration is small and can be used just as is only setting
 *   the correct package where to find your test classes:
 *   <pre>{@code
 *   <import resource="classpath:META-INF/joala/bdd/bdd-context.xml"/>
 *   <context:component-scan base-package="your.test.package"/>
 *   }</pre>
 *   </li>
 *   <li><em>Create a Spring Bean for Steps:</em> As we use Spring AOP you need to give Spring control over the
 *   step-methods. To do so we use an internal class with the strange looking name '_' (underscore). All for the
 *   sake to have readable tests later on:
 *   <pre>{@code
 *   &#64;Inject
 *   &#64;Singleton
 *   public Steps _;
 *   }</pre>
 *   </li>
 *   <li><em>Create a scenario:</em> Now let's start with a scenario. Use the following pattern:
 *   <pre>{@code
 *   &#64;Test
 *   public scenario_describe_title_with_underscores() throws Exception {
 *     ...
 *   }
 *   }</pre>
 *   </li>
 *   <li><em>Write your scenario steps:</em> Just type your scenario steps - even if they are read. Your IDE
 *   will assist you in creating the correct methods later on. We now use the Steps Bean which will later
 *   contain the step methods. Mind to name your references clearly in the steps:
 *   <pre>{@code
 *   &#64;Test
 *   public scenario_describe_title_with_underscores() throws Exception {
 *     _.given_there_is_a_HTML_document_D();
 *     _.when_the_HTML_document_D_contains_unicode_text_U();
 *     _.when_I_open_document_D_with_browser_B();
 *     _.then_browser_B_correctly_renders_text_U();
 *   }
 *   }</pre>
 *   </li>
 *   <li><em>Use References:</em> Now we need to carry the information from step to step about the different
 *   test artifacts. We use the statically imported method {@code ref()} from utility class {@code References}:
 *   <pre>{@code
 *   &#64;Test
 *   public scenario_describe_title_with_underscores() throws Exception {
 *     Reference<File> documentD = ref("D");
 *     Reference<String> textU = ref("U");
 *     Reference<WebDriver> browserB = ref("B");
 *
 *     _.given_there_is_a_HTML_document_D(documentD);
 *     _.when_the_HTML_document_D_contains_unicode_text_U(documentD, textU);
 *     _.when_I_open_document_D_with_browser_B(browserB);
 *     _.then_browser_B_correctly_renders_text_U(browserB, textU);
 *   }
 *   }</pre>
 *   </li>
 *   <li><em>Implement the steps:</em> Now let your IDE implement the missing methods and fill them with live.
 *   Now you are free to code as you like. The test body will always ensure that you have a good documentation
 *   of your test - for now and for ever.</li>
 *   <li><em>Configure SLF4j Logging:</em> Next you should configure logging in that way that the stories, scenarios
 *   and steps are logged. Assuming that you use Logback you can use the provided configuration file like this:
 *   <pre>{@code
 *   <?xml version="1.0" encoding="UTF-8"?>
 *   <configuration>
 *     <include resource="META-INF/joala/bdd/bdd-logback.xml"/>
 *     <root level="warn">
 *       ...
 *     </root>
 *   </configuration>
 *   }</pre>
 *   This configures the logging for BDD to be done to console. If you want to log the stories, scenarios and steps
 *   to a file appender in addition you can simply define additional loggers:
 *   <pre>{@code
 *   <?xml version="1.0" encoding="UTF-8"?>
 *   <configuration>
 *     <include resource="META-INF/joala/bdd/bdd-logback.xml"/>
 *     <logger name="${bdd.story.logger.name}" level="${bdd.log.level}">
 *       <appender-ref ref="myFileAppender"/>
 *     </logger>
 *     <logger name="${bdd.steps.logger.name}" level="${bdd.log.level}">
 *       <appender-ref ref="myFileAppender"/>
 *     </logger>
 *     <root level="warn">
 *       ...
 *     </root>
 *   </configuration>
 *   }</pre>
 *   </li>
 * </ol>
 * @see <a href="http://docs.behat.org/guides/1.gherkin.html">Behat: Writing Features - Gherkin Language</a>
 * @see <a href="http://code.google.com/p/spectacular/wiki/WritingBDDTests">spectacular: The format of BDD/Gherkin Tests</a>
 * @see <a href="http://cukes.info/">Cucumber</a>
 * @see <a href="http://jbehave.org/">JBehave</a>
 * @since 9/27/12
 */

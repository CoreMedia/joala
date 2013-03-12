# How to release Joala

## Release Preparations

* Ensure that you have configured GitHub for site deployment in your `settings.xml`:
    This is required in order to deploy the Maven site to [Joala GitHub Pages][].

    ```xml
    <server>
      <id>github-project-site</id>
      <!-- the username really is git, do not change this -->
      <username>git</username>
    </server>
    ```
    
    For deployment a fork of [wagon-gitsite][] by [kohsuke][kohsuke-wagon-gitsite] is used.
* Ensure that you have set the passwords for Maven Central Deployment as described in
    [Sonatype OSS Maven Repository Usage Guide][oss-usage] in your `settings.xml`.
    Mind that you need to have an account at [Sonatype's JIRA][sonatype-jira].
* Ensure that you have your GPG keys at hand and that you have published them to
    `hkp://pool.sks-keyservers.net/`.
* Ensure that you have installed Maven 3.0.4 or higher.

## Maven Release

Here are the recommended steps to release Joala:

```
joala$ mvn release:prepare release:perform
joala$ git push origin master --tags
```

As you can see from the commands above Joala is configured to release locally. Thus
it is required to push your changes after the release.

During the release you will by queried two times for your GPG Passphrase.

**Please don't release in batch mode** in order to choose the correct version numbers as
mentioned below. Versions for child modules will be set automatically so that you only
have to specify the versions once.

## Version Numbers

The version numbers of Joala are built of four elements (for reference and naming see
*[Maven: The Complete Reference, 3.3. POM Syntax][maven-reference-pom-syntax]*):

```
<major version>.<minor version>.<incremental version>-<qualifier>
```

### Qualifier

Use it for milestone builds like alpha, beta, ...

### Incremental Version

A change in the incremental version means a bugfix release. No features are introduced or API changed. You are
always save if you update the incremental version.

### Minor Version

Minor versions also might contain bugfixes but especially introduce new features. New feature are not necessarily
visible to the user of the Joala libraries. It might also mean better test coverage or enhancements to the build
infrastructure.

In minor versions deprecations might be introduced but existing API will not be broken.

### Major Release

Major releases might contain strong refactorings and possibly breaking API changes. Typically breaking API
changes are introduced by deprecations in previous minor version releases.

## Finishing Tasks

### Close/Promote Arifacts

If a staging repository is used, don't forget to close/promote the just released artifacts.

### Clean `checkout` folders

Your IDE might complain on additional git-folders after a release. They are located in `target/checkout`. In order to
make your IDE happy again, just call `mvn clean`.

## Troubleshooting

### Fix Version after Release

If you forgot to set the version correctly after release use the [versions-maven-plugin][] to adjust to the new
version:

```
joala$ mvn versions:set -DnewVersion=1.2.0-SNAPSHOT
```

### Rollback

Sometimes the for example the site deployment might fail. In this case the current
approach is to repeat the release. Assume you just were on your way release version `0.3.0` if Joala:

```
joala$ mvn release:rollback
joala$ git tag -d joala-bom-0.3.0
joala$ mvn release:prepare release:perform
```

<!-- Links -->

[Joala GitHub Pages]: <http://coremedia.github.com/joala/> "Joala GitHub Pages"
[versions-maven-plugin]: <http://mojo.codehaus.org/versions-maven-plugin/> "Codehaus.org: Versions Maven Plugin"
[maven-reference-pom-syntax]: <http://www.sonatype.com/books/mvnref-book/reference/pom-relationships-sect-pom-syntax.html> "Maven: The Complete Reference, 3.3. POM Syntax"
[oss-usage]: <https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide> "Sonatype OSS Maven Repository Usage Guide"
[sonatype-jira]: <https://issues.sonatype.org/> "Sonatype JIRA"
[wagon-gitsite]: <http://khuxtable.github.com/wagon-gitsite/> "Wagon Provider for GitHub Pages Site Deployment"
[kohsuke-wagon-gitsite]: <https://github.com/kohsuke/wagon-gitsite> "Fork of Wagon Provider for GitHub Pages Site Deployment"

# How to release Joala

## Release Preparations

Ensure that you have configured your GitHub credentials in your `settings.xml`. This is required in order to deploy
the Maven site to [Joala GitHub Pages][].

## Maven Release

Here are the recommended steps to release Joala:

```
joala$ git release:prepare release:perform
joala$ git push origin master --tags
```

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

## Cleanup Tasks

### Cleaning old Snapshot Site Versions

Change to branch `gh-pages` and delete the latest snapshot artifacts of the site below the site-folder:

```
site$ git rm -rf "*-SNAPSHOT"
site$ git commit -m "Delete old Snapshot Version of GitHub Pages Site"
site$ git push origin gh-pages
```

## Troubleshooting

### Fix Version after Release

If you forgot to set the version correctly after release use the [versions-maven-plugin][] to adjust to the new
version:

```
joala$ mvn versions:set -DnewVersion=1.2.0-SNAPSHOT
```

<!-- Links -->

[Joala GitHub Pages]: <http://coremedia.github.com/joala/> "Joala GitHub Pages"
[versions-maven-plugin]: <http://mojo.codehaus.org/versions-maven-plugin/> "Codehaus.org: Versions Maven Plugin"
[maven-reference-pom-syntax]: <http://www.sonatype.com/books/mvnref-book/reference/pom-relationships-sect-pom-syntax.html> "Maven: The Complete Reference, 3.3. POM Syntax"
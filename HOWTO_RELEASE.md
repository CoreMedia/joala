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

If you release a #.1.0 version you should update the development version to #.2.0-SNAPSHOT version. The last number
is only meant for critical bugfix releases. If you release a #.1.1 version (most likely on a branch) your next
development version should be #.1.2-SNAPSHOT.

If you release a 1.3.0 version the next development version is 1.4.0-SNAPSHOT. Changing the major release number
(leading number) will require manually creating a branch for the lower versions - while the development for the
next major release takes place on the master branch.

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
[versions-maven-plugin: <http://mojo.codehaus.org/versions-maven-plugin/set-mojo.html#newVersion> "Codehaus.org: Versions Maven Plugin"

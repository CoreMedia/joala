# Joala Maven Profiles

In here find an overview of profiles used within Joala.

## Profile: test-github-site

Activate this profile when testing to deploy the site as GitHub Pages. Might be wise to do before
release in order to ensure that you have set your credentials in your `settings.xml` correctly:

```
mvn clean install site-deploy -Ptest-github-site
```

## Profile: release

This profile is automatically triggered on release and ensures that the site is deployed as GitHub Pages
and the source and javadoc artifacts are deployed along with the other artifacts.
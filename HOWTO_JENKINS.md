# How to setup Jenkins CI Builds

## 3 Jobs

In order to provide fast reponses to code changes the recommended setup for Jenkins is having three distinct jobs.

### Job 1: joala-ci

Run the build with clean install. Possibly trigger joala-site after the job is done.

### Job 2: joala-site

Run the build with the following Maven goals and options:

```
clean site-deploy -Dlocal.site.deploy.url=file://${WORKSPACE}/local-deploy-site
```

`site-deploy` goal is used in order to workaround a behavior of Jenkins' MavenSiteArchiver which stores
all sites of sub-modules below `job/<job-name>/site/${project.artifactId}` also for sub-sub-modules. The
maven-site-plugin renders all links for sub-sub-modules as `${project.parent.artifactId}/${project.artifactId}`.
Thus links to those modules will be broken.

Instead with the configuration as given above you can add the following to your job description:

```xml
<a href="/job/joala-site/lastSuccessfulBuild/artifact/local-deploy-site/index.html">
  <img height="48" style="margin-right:1em" alt="" width="48" src="/static/80fe6139/images/48x48/help.gif">
</a>
<a href="/job/joala-site/lastSuccessfulBuild/artifact/local-deploy-site/index.html">Maven Site (working)</a>
```

which is actually the same as the link generated by Jenkins - having the link changed to the manually deployed site.

In order to make this work you need to activate archiving of the Maven site. Use this pattern for archiving files in
post-build actions:

```
**/local-deploy-site/**/*
```

### Job 3: joala-sonar

If you want to add some additional static code analysis like Sonar it is recommended to do this in an extra job in
order to keep CI builds fast and responsive.
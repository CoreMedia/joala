# Joala Release Notes, v1.2.0

## Thirdparty Updates

### General

* Joala artefacts are no longer available through _Maven Central_, nor is full documentation published as GitHub Pages.
* Compiler version changed to 17.

### Maven Plugins

* `org.apache.maven.plugins:maven-antrun-plugin`: 1.8 -> 3.1.0
* `org.apache.maven.plugins:maven-clean-plugin`: 3.0.0 -> 3.3.1
* `org.apache.maven.plugins:maven-compiler-plugin`: 3.6.1 -> 3.11.0
* `org.apache.maven.plugins:maven-dependency-plugin`: 3.0.1 -> 3.6.0
* `org.apache.maven.plugins:maven-deploy-plugin`: 2.8.2 -> 3.1.1
* `org.apache.maven.plugins:maven-enforcer-plugin`: 1.4.1 -> 3.4.0
* `org.apache.maven.plugins:maven-install-plugin`: 2.5.2 -> 3.1.1
* `org.apache.maven.plugins:maven-jar-plugin`: 3.0.2 -> 3.3.0
* `org.apache.maven.plugins:maven-javadoc-plugin`: 2.10.4 -> 3.5.0
* `org.apache.maven.plugins:maven-resources-plugin`: 3.0.2 -> 3.3.1
* `org.apache.maven.plugins:maven-site-plugin`: 3.6 -> 3.12.1
* `org.apache.maven.plugins:maven-surefire-plugin`: 2.20 -> 3.1.2
* `org.codehaus.mojo:exec-maven-plugin`: 1.6.0 -> 3.1.0

* Removed 
  * `org.codehaus.mojo:findbugs-maven-plugin`
  * `org.apache.maven.plugins:maven-checkstyle-plugin`
  * `org.apache.maven.plugins:maven-pmd-plugin`
  * `org.apache.maven.plugins:maven-javadoc-plugin`
  * `org.apache.maven.plugins:maven-jxr-plugin`
  * `org.apache.maven.plugins:maven-project-info-reports-plugin`
  * `org.apache.maven.plugins:maven-surefire-report-plugin`
  * `org.apache.maven.plugins:maven-source-plugin`
  * `org.apache.maven.plugins:maven-scm-plugin`
  * `org.apache.maven.plugins:maven-release-plugin`
  * `org.apache.maven.plugins:maven-gpg-plugin`
  * `org.codehaus.mojo:versions-maven-plugin`
  * `org.apache.maven.scm:maven-scm-provider-gitexe`
  * `org.apache.maven.scm:maven-scm-manager-plexus`

### Dependencies

* `org.apache.maven.doxia:doxia-module-markdown`: 1.7 -> 1.12.0
* `ch.qos.logback:logback-classic`: 1.2.3 -> 1.4.11
* `ch.qos.logback:logback-core`: 1.2.3 -> 1.4.11
* `com.google.guava:guava`: 22.0 -> 32.1.2-jre
* `junit:junit`: 4.12 -> 4.13.2
* `org.apache.commons:commons-lang3`: 3.6 -> 3.13.0
* `org.apache.commons:commons-text`: 1.1 -> 1.10.0
* `org.apache.httpcomponents:httpclient`: 4.5.3 -> 4.5.14
* `org.apache.httpcomponents:httpcore`: 4.4.6 -> 4.4.16
* `org.aspectj:aspectjweaver`: 1.8.10 -> 1.9.20.1
* `org.mockito:mockito-core`: 1.10.19 -> 5.5.0
* `org.objenesis:objenesis`:. 2.5.1 -> 3.3
* `org.powermock:powermock-api-mockito` 1.7.0 -> `org.powermock:powermock-api-mockito2` 2.0.9
* `org.powermock:powermock-api-support`:. 1.7.0 -> 2.0.9
* `org.powermock:powermock-core`: 1.7.0 -> 2.0.9
* `org.powermock:powermock-module-junit4`: 1.7.0 -> 2.0.9
* `org.slf4j:jcl-over-slf4j`: 1.7.25 -> 2.0.9
* `org.slf4j:slf4j-api`: 1.7.25 -> 2.0.9
* `org.springframework:spring-aop`: 4.3.9.RELEASE -> 5.3.29
* `org.springframework:spring-beans`: 4.3.9.RELEASE -> 5.3.29
* `org.springframework:spring-context`: 4.3.9.RELEASE -> 5.3.29
* `org.springframework:spring-context-support`: 4.3.9.RELEASE -> 5.3.29
* `org.springframework:spring-core`: 4.3.9.RELEASE -> 5.3.29
* `org.springframework:spring-test`: 4.3.9.RELEASE -> 5.3.29

* Removed dependency to `cglib:cglib` and `org.sonatype.oss:oss-parent`.

### Removed

* Removed unused modules `book`, `joala-dns`, `joala-image`, and `joala-expressions-library`.

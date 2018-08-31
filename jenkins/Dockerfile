FROM jenkins/jenkins:lts

### The following arguments are copy & paste from parent Dockerfile.
### Unfortunately, there is no way to inherit them, afaik.

ARG user=jenkins
ARG group=jenkins
ARG uid=1000
ARG gid=1000

### We don't need any security. Running only locally.
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

### >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
### Root Actions
### <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

USER root

### See https://github.com/taypo/jenkins/blob/master/Dockerfile

RUN apt-get update
RUN apt-get install -y maven openjdk-8-jdk

### >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
### Jenkins Actions
### <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

USER ${user}

RUN echo "Set up of /usr/share/jenkins/ref/"
COPY ref/ /usr/share/jenkins/ref/

### Install Jenkins Plugins

RUN echo "Installing plugins and their transitive dependencies:"
RUN cat "/usr/share/jenkins/ref/plugins.txt"
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

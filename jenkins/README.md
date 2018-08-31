# Jenkins for Joala Builds

This docker image will be set up to do the CI and release for Joala.

In order to set up run `joala-ci.sh`.

## Lessons Learned

### Bash for Windows

1. All path referenced in the container must be prefixed with "/" to escape them
   from Windows-Bash (git bash, etc.) to be transformed to local
   (windows drives). This is for example also true, if you want to execute bash
   on a container:
   
    ```bash
    $ docker exec -it ... //bin/bash
    ```
    Reference: [Stack Overflow][so50608301]
    
2. Mounting: Note, that you must first enable _Shared Drives_ in your Docker
   Settings. Otherwise the mounted directory will be silently empty.
   
### IntelliJ IDEA Docker Support

* In order to run Dockerfile directly within IntelliJ IDEA you need to
  configure Docker to expose the daemon on `tcp://localhost:2375` without
  TLS.

## References

* [Automating Jenkins Docker Setup | Technology Conversations][tc2017]
* [Building your first Docker image with Jenkins 2: Guide for developers — Get into DevOps][GIDO_JenkinsDocker]
* [Docker mounted volume adds ;C to end of windows path when translating from linux style path - Stack Overflow][so50608301]
* [Getting Started with Pipelines][JEN_Pipelines1]
* [How create and configure a new Jenkins Job using groovy? - Stack Overflow][so16963309]
* [jenkinsci/docker: Docker official jenkins repo][JEN_docker]

[GIDO_JenkinsDocker]: <https://getintodevops.com/blog/building-your-first-docker-image-with-jenkins-2-guide-for-developers> "Building your first Docker image with Jenkins 2: Guide for developers — Get into DevOps"
[JEN_docker]: <https://github.com/jenkinsci/docker> "jenkinsci/docker: Docker official jenkins repo"
[JEN_Pipelines1]: <https://jenkins.io/pipeline/getting-started-pipelines/> "Getting Started with Pipelines"
[so16963309]: <https://stackoverflow.com/questions/16963309/how-create-and-configure-a-new-jenkins-job-using-groovy> "How create and configure a new Jenkins Job using groovy? - Stack Overflow"
[so50608301]: <https://stackoverflow.com/questions/50608301/docker-mounted-volume-adds-c-to-end-of-windows-path-when-translating-from-linux> "Docker mounted volume adds ;C to end of windows path when translating from linux style path - Stack Overflow"
[tc2017]: <https://technologyconversations.com/2017/06/16/automating-jenkins-docker-setup/> "Automating Jenkins Docker Setup | Technology Conversations"

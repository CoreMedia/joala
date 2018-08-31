pipeline {
  agent any

  tools {
    // Requires MAVEN3 to be configured in Global Tool Configuration
    maven "MAVEN3"
    // Requires JDK8 to be configured in Global Tool Configuration
    jdk "JDK8"
  }

  // Note, that parameters can only be configured after first run.
  parameters {
    booleanParam(name: 'RELEASE', defaultValue: false, description: 'Check this to trigger a release build.')
  }

  triggers {
    pollSCM('@daily')
  }

  stages {
    stage('Preparation') {
      steps {
        git 'https://github.com/CoreMedia/joala.git'
      }
    }
    stage('Build') {
      steps {
        sh "'${M2_HOME}/bin/mvn' -Dmaven.test.failure.ignore clean install"
      }
    }
    stage('Release') {
      when {
        expression { return params.RELEASE }
      }
      steps {
        withCredentials([
                usernamePassword(credentialsId: 'sonatype-jira', usernameVariable: 'OSS_USER', passwordVariable: 'OSS_PASSWORD'),
                string(credentialsId: 'github-oauth', variable: 'GITHUB_OAUTH')
        ]) {
          echo "Release not implemented yet. Release would have been done as ${OSS_USER} with password ${OSS_PASSWORD} and GitHub OAUTH ${GITHUB_OAUTH}."
        }
      }
    }
  }
  post {
    always {
      junit '**/target/surefire-reports/TEST-*.xml'
      archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
    }
  }
}

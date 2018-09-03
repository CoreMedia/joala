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
        echo "In case of failure: Please ensure you have your gpg-sign certificate set up correctly."
        configFileProvider([configFile(fileId: 'release-settings', variable: 'MAVEN_SETTINGS')]) {
          withCredentials([
                  string(credentialsId: 'github-oauth', variable: 'GITHUB_OAUTH'),
                  string(credentialsId: 'gpg-password', variable: 'GPG_PASSWORD'),
                  file(credentialsId: 'gpg-secring', variable: 'GPG_SECRING')
          ]) {
            echo "Release not implemented yet."
            echo "The following will be hidden in log, it just shows the usage..."
            echo "    Configured GitHub OAUTH: ${GITHUB_OAUTH}"
            echo "    Configured GPG Sign: ${GPG_SECRING}, password: ${GPG_PASSWORD}"
            echo "    Configured settings file: ${MAVEN_SETTINGS}"
            sh "cat '${MAVEN_SETTINGS}'"
          }
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

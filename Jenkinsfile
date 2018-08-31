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
        echo "Release not implemented yet."
      }
    }
  }
  post {
    always {
      junit '**/target/surefire-reports/TEST-*.xml'
      archive '**/target/*.jar'
    }
  }
}

node {
  def mvnHome

  echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"

  stage('Preparation') { // for display purposes
    // Get some code from a GitHub repository
    git 'https://github.com/CoreMedia/joala.git'
    // Get the Maven tool.
    // ** NOTE: This 'MAVEN3' Maven tool must be configured
    // **       in the global configuration.
    mvnHome = tool 'MAVEN3'
  }
  stage('Build') {
    // Run the maven build
    if (isUnix()) {
      sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
    } else {
      bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
    }
  }
  stage('Results') {
    junit '**/target/surefire-reports/TEST-*.xml'
    archive 'target/*.jar'
  }
}

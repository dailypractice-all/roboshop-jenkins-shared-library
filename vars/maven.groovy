def call() {
  env.EXTRA_OPTS="-Dsonar.java.binaries=./target"
  node() {
    common.pipelineInit()

    stage('Build package') {
      sh '''
        mvn clean package
      '''
    }
    common.codeChecks()

    if (env.BRANCH_NAME == env.TAG_NAME) {
      common.publishArtifacts()
    }
  }
}
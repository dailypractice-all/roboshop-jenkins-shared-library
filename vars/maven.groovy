def call() {
  node() {
    common.pipelineInit()

    stage('Build package') {
      sh '''
        mvn clean package
      '''
    }
  }
}
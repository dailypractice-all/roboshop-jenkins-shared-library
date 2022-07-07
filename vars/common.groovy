def pipelineInit() {
  stage('Initiate Repo') {
    sh 'rm -rf *'
    git branch: 'main', url: "https://github.com/dailypractice-all/${COMPONENT}.git"
  }
}
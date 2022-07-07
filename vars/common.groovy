def pipelineInit() {
  stage('Initiate Repo') {
    sh 'rm -rf *'
    git branch: 'main', url: "https://github.com/dailypractice-all/${COMPONENT}.git"
  }
}

def publishArtifacts() {
  stage("Prepare Artifacts"){
    if (env.APP_TYPE == "nodejs") {
      sh """
        zip -r ${COMPONENT}.zip node_modules server.js
      """
    }
  }
}

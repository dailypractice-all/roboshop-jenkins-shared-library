def pipelineInit() {
  stage('Initiate Repo') {
    sh 'rm -rf *'
    git branch: 'main', url: "https://github.com/dailypractice-all/${COMPONENT}.git"
  }
}

def publishArtifacts() {
  stage("Prepare Artifacts") {
    if (env.APP_TYPE == "nodejs") {
      sh """
        zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
      """
    }
    if (env.APP_TYPE == "maven") {
      sh """
        cp target/${COMPONENT}-1.0.jar ${COMPONENT}.jar
        zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar
      """
    }
    if (env.APP_TYPE == "python") {
      sh """
        zip -r ${COMPONENT}-${TAG_NAME}.zip *.py ${COMPONENT}.ini requirements.txt
      """
    }
    if (env.APP_TYPE == "nginx") {
      sh """
        cd static 
        zip -r ../${COMPONENT}-${TAG_NAME}.zip *
      """
    }
  }

  stage('Push Artifacts to Nexus') {
    withCredentials([usernamePassword(credentialsId: 'NEXUS', passwordVariable: 'pass', usernameVariable: 'user')]) {
      sh """
        curl -v -u ${user}:${pass} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.11.249:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
      """
    }
  }
}

def codeChecks() {
  stage('Quality Checks & Unit Tests') {
    parallel([
        qualityChecks: {
          echo "hello"
        },
        unitTests: {
          unitTests()
        }
    ])
  }
}

def unitTests() {
    if (env.APP_TYPE == "nodejs") {
      sh """
        # npm run test
        echo Run test cases
      """
    }
    if (env.APP_TYPE == "maven") {
      sh """
       # mvn run test
       echo Run test cases
      """
    }
    if (env.APP_TYPE == "python") {
      sh """
        # python -m unittest
        echo Run test cases
      """
    }
    if (env.APP_TYPE == "nginx") {
      sh """
        # npm run test
        echo Run test cases
      """
    }
}
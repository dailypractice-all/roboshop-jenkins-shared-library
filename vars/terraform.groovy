def call() {
  node {
    properties([
      parameters([
        choice(choices: ['dev', 'prod'], description: "Choose Environment", name: "ENV"),
      ])
    ])
    ansiColor('xterm') {

      stage('Code Checkout') {
        sh 'find . | sed -e "1d" | xargs rm -rf'
        git branch: 'main', url: 'https://github.com/dailypractice-all/roboshop-terraform-mutable-approach.git'
      }
      stage('Terraform Init') {
        sh 'terraform init -backend-config=env/${ENV}-backend.tfvars'
      }
      stage('Terraform Validate') {
        sh 'terraform validate'
      }
      stage('terraform Plan') {
        sh 'terraform plan -var-file=env/${ENV}.tfvars'
      }
      stage('Terraform Apply') {
        sh 'terraform apply -auto-approve -var-file=env/${ENV}.tfvars'
      }
    }
  }
}
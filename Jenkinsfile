pipeline {
  agent {
    node {
      label 'master'
    }
  }
  
	//Get code
  stages {
    stage('Checkout code') {
      steps {
        git branch: 'devops-richard',
        url: 'https://github.com/The-Avengers-Gamera/gamera-be.git'
      }
    }
    
    stage('build') {
      steps {
	      sh "apt install make"
	      sh "make app_local_build "
        sh "./gradlew clean build"
    	}
    }
    
    stage('deploy') {
      steps {
        echo 'deploy stage'
      }
    }
  }
  post {
    always{
      echo 'Success'
    }
  }
    failure {
      echo 'Mission Fail'
    }
}

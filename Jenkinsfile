pipeline {
  agent {
    node {
      label 'master'
    }
  }
  environment {

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
        steps {
        	sh "./gradlew clean build"
      	}
    }
    
    stage('deploy') {
      steps {
        
      }
    }
  }
}

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
        
      }
    }
    
    stage('deploy') {
      steps {
        
      }
    }
  }
}

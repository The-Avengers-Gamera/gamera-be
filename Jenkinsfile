pipeline {
	
  agent any
  
	//Get code
  stages {
    stage('Checkout code') {
      steps {
        git branch: 'devops-ronald',
        url: 'https://github.com/The-Avengers-Gamera/gamera-be.git'
      }
    }
    
    stage('build') {
      steps {
        sh "./gradlew clean build"
    	}
    }
    stage('Test'){
       steps{
         echo 'Testing...'
         sh './gradlew clean test'
       }
    }
    stage('deploy') {
      steps {
        echo 'deploy stage'
      }
    }
  }
  post {
     	always {
    		echo 'Pipeline completed'
  	}
  	failure {
    		echo 'Pipeline failed'
  	}
  }
}

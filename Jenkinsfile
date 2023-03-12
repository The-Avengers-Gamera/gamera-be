pipeline {
	
  agent any
	
  environment {
        DB_URL = credentials('DB_URL')
        DB_USERNAME = credentials('DB_USERNAME')
        DB_PASSWORD = credentials('DB_PASSWORD')
        ECR_PASSWORD_STDIN = credentials('ECR_PASSWORD_STDIN')
    }
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
        sh "./gradlew build"
    	}
    }
    stage('Test'){
       steps{
         echo 'Testing...'
         sh './gradlew clean test'
       }
    }
    stage('Deploy') {
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

pipeline {
  agent {
    node {
      label 'master'
    }
  }
  environment {
    DOCKER_REGISTRY = "docker.example.com"
    DOCKER_COMPOSE_VERSION = "1.29.2"
    APP_NAME = "myapp"
    APP_VERSION = "1.0.0"
    POSTGRES_VERSION = "13.3"
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

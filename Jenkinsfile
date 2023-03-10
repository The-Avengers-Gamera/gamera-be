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
        sh "./gradlew clean build"
    	}
    }
    stage('Test'){
       steps{
         echo 'Testing...'
         sh './gradlew clean test'
       }
    }
    stage('Deploy') {
  	  steps {
        echo 'Deploying...'

        // Generate docker image from Dockerfile
        sh """sudo docker build \
              --build-arg DB_URL=${DB_URL} \
              --build-arg DB_USERNAME=${DB_USERNAME} \
              --build-arg DB_PASSWORD=${DB_PASSWORD} \
              -t gamera-service .
           """

        // Authenticate Docker client to ECR repository
        withCredentials([usernamePassword(credentialsId: 'ecr-credentials', passwordVariable:     'ECR_PASSWORD_STDIN', usernameVariable: 'ECR_USERNAME')]) {
          sh "aws ecr get-login-password --region ap-southeast-2 | docker login --username    AWS --password-stdin ${ECR_USERNAME}"
        }

        // Tag the docker image with version and latest tag
        def versionTag = "${ECR_USERNAME}/gamera-repository:${BUILD_NUMBER}"
        def latestTag = "${ECR_USERNAME}/gamera-repository:latest"
        sh "docker tag gamera-service ${versionTag}"
        sh "docker tag gamera-service ${latestTag}"

        // Push the Docker image to ECR
        sh "docker push ${versionTag}"
        sh "docker push ${latestTag}"

        // Redeploy the service to ECS cluster with new task definition
        sh "aws ecs update-service \
              --cluster DevGameraCluster \
              --service gamera-uat \
              --force-new-deployment"
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

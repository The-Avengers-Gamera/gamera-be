pipeline {
    agent {
        //Use high performance node in GCP to build, test and deploy project
        label 'gcp_node'
    }

    environment {
        //Pass in environmental variables
        //The credentials are stored in Jenkins credentials
        DB_URL = credentials('DB_URL')
        DB_USERNAME = credentials('DB_USERNAME')
        DB_PASSWORD = credentials('DB_PASSWORD')
    }

    stages {
        stage('Git checkout') {
            steps {
                //Get the source code to workspace
                echo 'Getting the source code...'
                git branch: 'devops-richard',
                url: 'https://github.com/The-Avengers-Gamera/gamera-be.git'
            }
        }

        stage('Build') {
            steps {
                //Build the project
                echo 'Building project...'
                sh './gradlew build'
            }
        }

        stage('Test') {
            steps {
                //Unit test to check if testcases can be passed
                echo 'Testing...'
                sh './gradlew clean test'
            }
        }

        stage('Deploy') {
            steps {
                /*
                    Deploy the project to cloud infrastruce
                    First, generate docker image from Dockerfile
                    Then, push the Dockerfile to ECR
                    Last, user ECS to pull the image and run the service
                */
                echo 'Deploying...'
                sh 'docker build -t gamera-service .'
            }
        }
    }
}
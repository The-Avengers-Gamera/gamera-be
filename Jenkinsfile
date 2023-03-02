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
                echo 'Deploying...'
                //Generate docker image from Dockerfile
                //Use jenkins credentials as input args
                sh 'sudo docker build --build-arg DB_URL=${DB_URL} \
                    --build-arg DB_USERNAME=${DB_USERNAME} \
                    --build-arg DB_PASSWORD=${DB_PASSWORD} -t gamera_be .'

                //push the Dockerfile to ECR
                sh 'docker tag gamera_be:latest ${ECR_PASSWORD_STDIN}'
                sh 'docker push ${ECR_PASSWORD_STDIN}'

                //After the docker is uploaded, delete the local docker image
                sh 'docker images -qa | xargs docker image rm'
                
                /*    
                    Last, user ECS to pull the image and run the service
                */
            }
        }
    }
}

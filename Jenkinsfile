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
        ECR_TOKEN_UAT = credentials('ECR_TOKEN_UAT')
    }

    stages {
        stage('Git checkout') {
            steps {
                //Get the source code to workspace
                echo 'Getting the source code...'
                git branch: 'main',
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
                sh 'sudo docker build --build-arg DB_URL=${DB_URL} \
                    --build-arg DB_USERNAME=${DB_USERNAME} \
                    --build-arg DB_PASSWORD=${DB_PASSWORD} -t gamera-service .'

                //Authenticate Docker client to ECR repository
                sh 'aws ecr get-login-password --region ap-southeast-2 | docker login \
                    --username AWS --password-stdin ${ECR_TOKEN_UAT}'

                //Tag the docker built, one for version tag, one for latest tag
                sh 'docker tag gamera-service ${ECR_TOKEN_UAT}/gamera-repository-uat:${BUILD_NUMBER}'
                sh 'docker tag gamera-service ${ECR_TOKEN_UAT}/gamera-repository-uat:latest'

                //push the Dockerfile to ECR
                sh 'docker push ${ECR_TOKEN_UAT}/gamera-repository-uat:${BUILD_NUMBER}'
                sh 'docker push ${ECR_TOKEN_UAT}/gamera-repository-uat:latest'

                //Redeploy the service to ECS cluster with new task defination
                sh 'aws ecs update-service --cluster DevGameraCluster \
                    --service gamera-uat --force-new-deployment'
            }
        }
    }

    post {
        always {
            //Clean the workspace after every pipeline build process
            echo 'Cleaning workspace'
            sh 'docker images gamera-service -q | xargs docker image rm -f'
            cleanWs()
        }

        failure {
            //Send error messages to developer to debug
            echo 'Build failed, sending error message to developer'
        }
    }
}

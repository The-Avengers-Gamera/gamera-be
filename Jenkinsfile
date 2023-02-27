pipeline {
    agent {
        label 'gcp_node'
    }

    stages {
        stage('Git checkout') {
            steps {
                git branch: 'devops-richard',
                url: 'https://github.com/The-Avengers-Gamera/gamera-be.git'
            }
        }

        stage('Build') {
            steps {
                sh 'sudo make app_local_compose_up'
                sh 'sudo app_local_build'
            }
        }

        stage('Test') {
            steps {
                echo 'Testing'
                //sh 'make app_local_test'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying'
            }
        }
    }
}
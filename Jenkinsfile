pipeline {
    agent none
    options { skipDefaultCheckout(true) }
    stages {
        stage('Build and Test') {
            agent {
                docker {
                    image 'gradle:latest' // Gradle 이미지로 변경
                    args '-v /root/.gradle:/root/.gradle' // Maven 경로를 Gradle 경로로 변경
                }
            }
            options { skipDefaultCheckout(false) }
            steps {
                sh 'gradle build' // Maven 명령어를 Gradle 명령어로 변경
            }
        }
        stage('Docker build') {
            agent any
            steps {
                sh 'docker build -t weffy_back:latest backend/'
            }
        }
        stage('Docker run') {
            agent any
            steps {
                sh 'docker ps -f name=weffy_back -q \
                    | xargs --no-run-if-empty docker container stop'
                sh 'docker container ls -a -f name=weffy_back -q \
                    | xargs -r docker container rm'
                sh 'docker images -f "dangling=true" -q \
                    | xargs -r docker rmi'
                sh 'docker run -d --name weffy_back -p 8080:8081 weffy_back:latest'
            }
        }
    }
}


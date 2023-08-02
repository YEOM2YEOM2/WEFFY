pipeline {
    agent none
    options { skipDefaultCheckout(true) }
    environment {
        AWS_CREDENTIALS = credentials('aws-id')
    }
    stages {
        stage('Prepare credentials') {
            agent any
            steps {
                withCredentials([
                    file(credentialsId: 'application-dev.properties', variable: 'PROP_FILE'),
		    string(credentialsId: 'aws-access-key', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-key', variable: 'AWS_SECRET_ACCESS_KEY')
                ]) {
                    // The credentials can be used within this block
                    sh 'cp $PROP_FILE backend/src/main/resources/application-dev.properties' // Copy the secret file to the current directory
                }
            }
        }
        stage('Build and Test') {
            agent {
                docker {
                    image 'weffy_back' // Replace with the name of your custom image
                    args "-v gradle-${env.BUILD_TAG}:/root/.gradle"
                }
            }
            options { skipDefaultCheckout(false) }
            steps {
                sh 'cd backend && ./gradlew build -x test'
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
                sh 'docker run -d -p 8081:8081 --name weffy_back -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} weffy_back:latest'

            }
        }
    }

}


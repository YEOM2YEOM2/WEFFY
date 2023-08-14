pipeline {
    agent none
    options { skipDefaultCheckout(true) }
    	stage('Cleanup Workspace') {
    steps {
        sh 'rm -rf *'
    }
}
stage('Clone Repository') {
    steps {
        checkout scm
    }
}

	stages {
        stage('Prepare credentials') {
            agent any
            steps {
                withCredentials([
                    file(credentialsId: 'application-dev.properties', variable: 'PROP_FILE')
                ]) {
                    // The credentials can be used within this block
                    sh 'cp $PROP_FILE backend/authentication-integration-service/src/main/resources/application-dev.properties'
                }
            }
        }
        stage('Build and Test') {
            agent {
                docker {
                    image 'authentication-integration-service' // Replace with the name of your custom image
                    args "-v gradle-${env.BUILD_TAG}:/root/.gradle"
                }
            }
            options { skipDefaultCheckout(false) }
            steps {
                sh 'cd backend/authentication-integration-service && ./gradlew clean build -x test'
            }
        }
        stage('Docker build') {
            agent any
            steps {
                sh 'docker build -t authentication-integration-service:latest backend/authentication-integration-service/'
            }
        }
        stage('Docker run') {
            agent any
            steps {
                script {
                    sh 'docker ps -f name=authentication-integration-service -q \
                        | xargs --no-run-if-empty docker container stop'
                    sh 'docker container ls -a -f name=authentication-integration-service -q \
                        | xargs -r docker container rm'
                    sh 'docker images -f "dangling=true" -q \
                        | xargs -r docker rmi'
                }
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-id']]) {
                    sh 'docker run -d -p 8081:8081 --name authentication-integration-service -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY authentication-integration-service:latest'
                }
            }
        }
    }
}


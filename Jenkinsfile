pipeline {
    agent none
    options { skipDefaultCheckout(true) }
	stages {
        stage('Cleanup Workspace') {
            agent any
            steps {
                sh 'rm -rf *'
            }
        }
        stage('Clone Repository') {
            agent any
            steps {
                checkout scm
            }
        }
        stage('Prepare credentials') {
            agent any
            steps {
                withCredentials([
                    file(credentialsId: 'auth-application-dev.properties', variable: 'AUTH_FILE'),
                    // file(credentialsId: 'ov-content-application-dev.properties', variable: 'OV_CONTENT_FILE')
                ]) {
                    // The credentials can be used within this block
                    sh 'cp $AUTH_FILE backend/authentication-integration-service/src/main/resources/application-dev.properties'
                    // sh 'cp $OV_CONTENT_FILE backend/openvidu-content-service/src/main/resources/application-dev.properties'
                }
            }
        }
        stage('Build and Test for authentication-integration-service') {
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
        // stage('Build and Test for openvidu-content-service') {
        //     agent {
        //         docker {
        //             image 'openvidu-content-service' // Replace with the name of your custom image for openvidu-content-service
        //             args "-v gradle-${env.BUILD_TAG}:/root/.gradle"
        //         }
        //     }
        //     options { skipDefaultCheckout(false) }
        //     steps {
        //         sh 'cd backend/openvidu-content-service && ./gradlew clean build -x test'
        //     }
        // }

        stage('Docker build for authentication-integration-service') {
            agent any
            steps {
                sh 'docker build -t authentication-integration-service:latest backend/authentication-integration-service/'
            }
        }

        // stage('Docker build for openvidu-content-service') {
        //     agent any
        //     steps {
        //         sh 'docker build -t openvidu-content-service:latest backend/openvidu-content-service/'
        //     }
        // }
        stage('Docker run for authentication-integration-service') {
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
        // stage('Docker run for openvidu-content-service') {
        // agent any
        // steps {
        //     script {
        //         sh 'docker ps -f name=openvidu-content-service -q \
        //             | xargs --no-run-if-empty docker container stop'
        //         sh 'docker container ls -a -f name=openvidu-content-service -q \
        //             | xargs -r docker container rm'
        //         sh 'docker images -f "dangling=true" -q \
        //             | xargs -r docker rmi'
        //     }
        //     withCredentials([$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-id-for-openvidu']) {
        //         sh 'docker run -d -p 8083:8083 --name openvidu-content-service -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY openvidu-content-service:latest'
        //     }
        // }
        // }
    }
}


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
                    file(credentialsId: 'ov-content-application-dev.properties', variable: 'OV_CONTENT_FILE'),
                    file(credentialsId: 'ov-meeting-application-dev.properties', variable: 'OV_MEETING_FILE')
                ]) {
                    sh 'cp $AUTH_FILE backend/authentication-integration-service/src/main/resources/application-dev.properties'
                    sh 'cp $OV_CONTENT_FILE backend/openvidu-content-service/src/main/resources/application-dev.properties'
                    sh 'cp $OV_MEETING_FILE backend/openvidu-meeting-service/src/main/resources/application-dev.properties'
                }
            }
        }

        stage('Set Execute Permission for Gradlew') {
            agent any
            steps {
                sh 'chmod +x backend/authentication-integration-service/gradlew'
                sh 'chmod +x backend/openvidu-meeting-service/gradlew'
                sh 'chmod +x backend/openvidu-content-service/gradlew'
                sh 'chmod +x backend/mattermost-content-service/gradlew'
            }
        }

        // Project Build

        stage('Build and Test for authentication-integration-service') {
            agent {
                docker {
                    image 'authentication-integration-service'
                    args "-v gradle-${env.BUILD_TAG}:/root/.gradle"
                }
            }
            steps {
                sh 'cd backend/authentication-integration-service && ./gradlew clean build -x test'
            }
        }

        stage('Build and Test for openvidu-meeting-service') {
            agent {
                docker {
                    image 'openvidu-meeting-service'
                    args "-v gradle-${env.BUILD_TAG}:/root/.gradle"
                }
            }
            steps {
                sh 'cd backend/openvidu-meeting-service && ./gradlew clean build -x test'
            }
        }

        stage('Build and Test for openvidu-content-service') {
            agent {
                docker {
                    image 'openvidu-content-service'
                    args "-v gradle-${env.BUILD_TAG}:/root/.gradle"
                }
            }
            steps {
                sh 'cd backend/openvidu-content-service && ./gradlew clean build -x test'
            }
        }
        stage('Build and Test for mattermost-content-service') {
            // agent {
            //     docker {
            //         image 'mattermost-content-service'
            //         args "-v gradle-${env.BUILD_TAG}:/root/.gradle"
            //     }
            // }
            steps {
                sh 'cd backend/mattermost-content-service && ./gradlew clean build -x test'
            }
        }

        // Docker build start

        stage('Docker build for authentication-integration-service') {
            agent any
            steps {
                sh 'docker build -f backend/authentication-integration-service/Dockerfile -t authentication-integration-service:latest backend/authentication-integration-service/'
            }
        }

        stage('Docker build for openvidu-meeting-service') {
            agent any
            steps {
                sh 'docker build -f backend/openvidu-meeting-service/Dockerfile -t openvidu-meeting-service:latest backend/openvidu-meeting-service/'
            }
        }

        stage('Docker build for openvidu-content-service') {
            agent any
            steps {
                sh 'docker build -f backend/openvidu-content-service/Dockerfile -t openvidu-content-service:latest backend/openvidu-content-service/'
            }
        }

        stage('Docker build for mattermost-content-service') {
            agent any
            steps {
                sh 'docker build -f backend/mattermost-content-service/Dockerfile -t mattermost-content-service:latest backend/mattermost-content-service/'
            }
        }

        // Docker login

        stage('Login to Docker Hub') {
            agent any
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS')]) {
                    sh 'docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASS'
                }
            }
        }

        // Docker run

        stage('Docker run for authentication-integration-service') {
            agent any
            steps {
                script {
                    def stopStatus = sh(script: 'docker ps -f name=authentication-integration-service -q | xargs --no-run-if-empty docker container stop', returnStatus: true)
                    if (stopStatus != 0) {
                        echo "Failed to stop containers."
                    }

                    def rmStatus = sh(script: 'docker container ls -a -f name=authentication-integration-service -q | xargs -r docker container rm', returnStatus: true)
                    if (rmStatus != 0) {
                        echo "Failed to remove containers."
                    }

                    def rmiStatus = sh(script: 'docker images -f "dangling=true" -q | xargs -r docker rmi', returnStatus: true)
                    if (rmiStatus != 0) {
                        echo "Failed to remove dangling images."
                    }
                }

                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-id']]) {
                    sh 'docker run -d -p 8081:8081 --name authentication-integration-service -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY authentication-integration-service:latest'
                }
            }
        }
        stage('Docker run for openvidu-meeting-service') {
            agent any
            steps {
                script {
                    def stopStatus = sh(script: 'docker ps -f name=openvidu-meeting-service -q | xargs --no-run-if-empty docker container stop', returnStatus: true)
                    if (stopStatus != 0) {
                        echo "Failed to stop containers."
                    }

                    def rmStatus = sh(script: 'docker container ls -a -f name=openvidu-meeting-service -q | xargs -r docker container rm', returnStatus: true)
                    if (rmStatus != 0) {
                        echo "Failed to remove containers."
                    }

                    def rmiStatus = sh(script: 'docker images -f "dangling=true" -q | xargs -r docker rmi', returnStatus: true)
                    if (rmiStatus != 0) {
                        echo "Failed to remove dangling images."
                    }
                }

                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-id']]) {
                    sh 'docker run -d -p 8082:8082 --name openvidu-meeting-service -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY openvidu-meeting-service:latest'
                }
            }
        }

        stage('Docker run for openvidu-content-service') {
            agent any
            steps {
                script {
    
                    def stopStatus = sh(script: 'docker ps -f name=openvidu-content-service -q | xargs --no-run-if-empty docker container stop', returnStatus: true)
                    if (stopStatus != 0) {
                        echo "Failed to stop containers."
                    }
                    def rmStatus = sh(script: 'docker container ls -a -f name=openvidu-content-service -q | xargs -r docker container rm', returnStatus: true)
                    if (rmStatus != 0) {
                        echo "Failed to remove containers."
                    }

        
                    def rmiStatus = sh(script: 'docker images -f "dangling=true" -q | xargs -r docker rmi', returnStatus: true)
                    if (rmiStatus != 0) {
                        echo "Failed to remove dangling images."
                    }
                }

                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-id']]) {
                    sh 'docker run -d -p 8083:8083 --name openvidu-content-service -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY openvidu-content-service:latest'
                }
            }
        }
        stage('Docker run for mattermost-content-service') {
            agent any
            steps {
                script {
            
                    def stopStatus = sh(script: 'docker ps -f name=mattermost-content-service -q | xargs --no-run-if-empty docker container stop', returnStatus: true)
                    if (stopStatus != 0) {
                        echo "Failed to stop containers."
                    }

                    def rmStatus = sh(script: 'docker container ls -a -f name=mattermost-content-service -q | xargs -r docker container rm', returnStatus: true)
                    if (rmStatus != 0) {
                        echo "Failed to remove containers."
                    }


                    def rmiStatus = sh(script: 'docker images -f "dangling=true" -q | xargs -r docker rmi', returnStatus: true)
                    if (rmiStatus != 0) {
                        echo "Failed to remove dangling images."
                    }
                }

                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-id']]) {
                    sh 'docker run -d -p 8084:8084 --name mattermost-content-service -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY mattermost-content-service:latest'
                }
            }
        }
    }
}
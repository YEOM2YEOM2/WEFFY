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
                    file(credentialsId: 'ov-content-application-dev.properties', variable: 'OV_CONTENT_FILE')
                ]) {
                    sh 'cp $AUTH_FILE backend/authentication-integration-service/src/main/resources/application-dev.properties'
                    sh 'cp $OV_CONTENT_FILE backend/openvidu-content-service/src/main/resources/application-dev.properties'
                }
            }
        }

        stage('Set Execute Permission for Gradlew') {
            agent any
            steps {
                sh 'chmod +x backend/authentication-integration-service/gradlew'
                sh 'chmod +x backend/openvidu-content-service/gradlew'
            }
        }

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

        stage('Docker build for authentication-integration-service') {
            agent any
            steps {
                sh 'docker build -f backend/authentication-integration-service/Dockerfile -t authentication-integration-service:latest backend/authentication-integration-service/'
            }
        }

        stage('Docker build for openvidu-content-service') {
            agent any
            steps {
                sh 'docker build -f backend/openvidu-content-service/Dockerfile -t openvidu-content-service:latest backend/openvidu-content-service/'
            }
        }

        stage('Login to Docker Hub') {
            agent any
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS')]) {
                    sh 'docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASS'
                }
            }
        }

        stage('Docker run for authentication-integration-service') {
            agent any
            steps {
                script {
                    // 컨테이너 정지
                    def stopStatus = sh(script: 'docker ps -f name=authentication-integration-service -q | xargs --no-run-if-empty docker container stop', returnStatus: true)
                    if (stopStatus != 0) {
                        echo "Failed to stop containers. They might not be running, which is okay. Continuing..."
                    }

                    // 컨테이너 삭제
                    def rmStatus = sh(script: 'docker container ls -a -f name=authentication-integration-service -q | xargs -r docker container rm', returnStatus: true)
                    if (rmStatus != 0) {
                        echo "Failed to remove containers. They might not exist, which is okay. Continuing..."
                    }

                    // 이미지 삭제
                    def rmiStatus = sh(script: 'docker images -f "dangling=true" -q | xargs -r docker rmi', returnStatus: true)
                    if (rmiStatus != 0) {
                        echo "Failed to remove dangling images. They might not exist, which is okay. Continuing..."
                    }
                }

                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-id']]) {
                    sh 'docker run -d -p 8081:8081 --name authentication-integration-service -e AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID -e AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY authentication-integration-service:latest'
                }
            }
        }
        stage('Docker run for openvidu-content-service') {
            agent any
            steps {
                script {
                    // 컨테이너 정지
                    def stopStatus = sh(script: 'docker ps -f name=openvidu-content-service -q | xargs --no-run-if-empty docker container stop', returnStatus: true)
                    if (stopStatus != 0) {
                        echo "Failed to stop containers."
                    }

                    // 컨테이너 삭제
                    def rmStatus = sh(script: 'docker container ls -a -f name=openvidu-content-service -q | xargs -r docker container rm', returnStatus: true)
                    if (rmStatus != 0) {
                        echo "Failed to remove containers."
                    }

                    // 이미지 삭제
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
    }
}

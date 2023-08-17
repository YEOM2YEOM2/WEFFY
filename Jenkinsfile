pipeline {
    agent any

    stages {
        
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

         stage('Prepare credentials') {
            agent any
            steps {
                withCredentials([
                    file(credentialsId: 'auth-application-dev.properties', variable: 'AUTH_FILE'),
                    file(credentialsId: 'ov-content-application-dev.properties', variable: 'OV_CONTENT_FILE'),
                    file(credentialsId: 'ov-meeting-application-dev.properties', variable: 'OV_MEETING_FILE')
                ]) 
                    sh "cp \$AUTH_FILE backend/authentication-integration-service/src/main/resources/application-dev.properties"
                    sh "cp \$OV_CONTENT_FILE backend/openvidu-content-service/src/main/resources/application-dev.properties"
                    sh "cp \$OV_MEETING_FILE backend/openvidu-meeting-service/src/main/resources/application-dev.properties"
                }
            }
        }


        stage('Set Execute Permission for Gradlew') {
            steps {
                sh 'chmod +x backend/authentication-integration-service/gradlew'
                sh 'chmod +x backend/openvidu-meeting-service/gradlew'
                sh 'chmod +x backend/openvidu-content-service/gradlew'
                sh 'chmod +x backend/mattermost-content-service/gradlew'
            }
        }

        stage('Build and Test openvidu-meeting-service') {
            steps {
                dir('backend/openvidu-meeting-service') {
                    sh './gradlew clean build -x test'
                }
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
                sh 'chmod +x backend/authentication-integration-service/gradlew'
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
                sh 'chmod +x backend/openvidu-content-service/gradlew'
                sh 'cd backend/openvidu-content-service && ./gradlew clean build -x test'
            }
        }
        stage('Build and Test for mattermost-content-service') {
            agent {
                docker {
                    image 'mattermost-content-service'
                    args "-v gradle-${env.BUILD_TAG}:/root/.gradle"
                }
            }
            steps {
                sh 'chmod +x backend/mattermost-content-service/gradlew'
                sh 'cd backend/mattermost-content-service && ./gradlew clean build -x test'
            }
        }

        stage('Docker build and push') {
            parallel {
                stage('Docker build and push authentication-integration-service') {
                    steps {
                        withCredentials([
                            usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS')
                        ]) {
                            dir('backend/authentication-integration-service') {
                                sh 'docker build -t authentication-integration-service:latest .'
                                sh 'docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASS'
                                sh 'docker tag authentication-integration-service:latest kathyleesh/authentication-integration-service:latest'
                                sh 'docker push kathyleesh/authentication-integration-service:latest'
                            }
                        }
                    }
                }

                stage('Docker build and push openvidu-meeting-service') {
                    steps {
                        withCredentials([
                            usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS')
                        ]) {
                            dir('backend/openvidu-meeting-service') {
                                sh 'docker build -t openvidu-meeting-service:latest .'
                                sh 'docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASS'
                                sh 'docker tag openvidu-meeting-service:latest kathyleesh/openvidu-meeting-service:latest'
                                sh 'docker push kathyleesh/openvidu-meeting-service:latest'
                            }
                        }
                    }
                }

                stage('Docker build and push openvidu-content-service') {
                    steps {
                        withCredentials([
                            usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS')
                        ]) {
                            dir('backend/openvidu-content-service') {
                                sh 'docker build -t openvidu-content-service:latest .'
                                sh 'docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASS'
                                sh 'docker tag openvidu-content-service:latest kathyleesh/openvidu-content-service:latest'
                                sh 'docker push kathyleesh/openvidu-content-service:latest'
                            }
                        }
                    }
                }

                stage('Docker build and push mattermost-content-service') {
                    steps {
                        withCredentials([
                            usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS')
                        ]) {
                            dir('backend/mattermost-content-service') {
                                sh 'docker build -t mattermost-content-service:latest .'
                                sh 'docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASS'
                                sh 'docker tag mattermost-content-service:latest kathyleesh/mattermost-content-service:latest'
                                sh 'docker push kathyleesh/mattermost-content-service:latest'
                            }
                        }
                    }
                }
            }          
        }

        stage('Docker run') {
            parallel {
                stage('Docker run authentication-integration-service') {
                    steps {
                        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-id']]) {
                            sh "docker run -d -p 8081:8081 --name authentication-integration-service -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} kathyleesh/authentication-integration-service:latest"
                        }
                    }
                }
                stage('Docker run openvidu-meeting-service') {
                    steps {
                        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-id']]) {
                            sh "docker run -d -p 8082:8082 --name openvidu-meeting-service -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} kathyleesh/openvidu-meeting-service:latest"
                        }
                    }
                }
                stage('Docker run openvidu-content-service') {
                    steps {
                        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-id']]) {
                            sh "docker run -d -p 8083:8083 --name openvidu-content-service -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} kathyleesh/openvidu-content-service:latest"
                        }
                    }
                }
                stage('Docker run mattermost-content-service') {
                    steps {
                        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-id']]) {
                            sh "docker run -d -p 8084:8084 --name mattermost-content-service -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} kathyleesh/mattermost-content-service:latest"
                        }
                    }
                }
            }
        }

    }
}
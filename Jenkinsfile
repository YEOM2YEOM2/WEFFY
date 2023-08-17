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
                    file(credentialsId: 'ov-meeting-application-dev.properties', variable: 'OV_MEETING_FILE'),
                    usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS'),
                    [$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-id']
                ]) {
                    sh 'cp $AUTH_FILE backend/authentication-integration-service/src/main/resources/application-dev.properties'
                    sh 'cp $OV_CONTENT_FILE backend/openvidu-content-service/src/main/resources/application-dev.properties'
                    sh 'cp $OV_MEETING_FILE backend/openvidu-meeting-service/src/main/resources/application-dev.properties'
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

        stage('Build and Test Projects') {
            parallel {
                stage('Build and Test authentication-integration-service') {
                    steps {
                        dir('backend/authentication-integration-service') {
                            sh './gradlew clean build -x test'
                        }
                    }
                }
                stage('Build and Test openvidu-content-service') {
                    steps {
                        dir('backend/openvidu-content-service') {
                            sh './gradlew clean build -x test'
                        }
                    }
                }
                stage('Build and Test mattermost-content-service') {
                    steps {
                        dir('backend/mattermost-content-service') {
                            sh './gradlew clean build -x test'
                        }
                    }
                }
            }
        }

        stage('Build and Test openvidu-meeting-service') {
            steps {
                dir('backend/openvidu-meeting-service') {
                    sh './gradlew clean build -x test'
                }
            }
        }


        stage('Docker build and push') {
            parallel {
                stage('Docker build and push authentication-integration-service') {
                    steps {
                        dir('backend/authentication-integration-service') {
                            sh 'docker build -t authentication-integration-service:latest .'
                            sh 'docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASS'
                            sh 'docker push authentication-integration-service:latest'
                        }
                    }
                }
                stage('Docker build and push openvidu-meeting-service') {
                    steps {
                        dir('backend/openvidu-meeting-service') {
                            sh 'docker build -t openvidu-meeting-service:latest .'
                            sh 'docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASS'
                            sh 'docker push openvidu-meeting-service:latest'
                        }
                    }
                }
                stage('Docker build and push openvidu-content-service') {
                    steps {
                        dir('backend/openvidu-content-service') {
                            sh 'docker build -t openvidu-content-service:latest .'
                            sh 'docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASS'
                            sh 'docker push openvidu-content-service:latest'
                        }
                    }
                }
                stage('Docker build and push mattermost-content-service') {
                    steps {
                        dir('backend/mattermost-content-service') {
                            sh 'docker build -t mattermost-content-service:latest .'
                            sh 'docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASS'
                            sh 'docker push mattermost-content-service:latest'
                        }
                    }
                }
            }
        }

        stage('Docker run') {
            parallel {
                stage('Docker run authentication-integration-service') {
                    steps {
                        sh "docker run -d -p 8081:8081 --name authentication-integration-service -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} authentication-integration-service:latest"
                    }
                }
                stage('Docker run openvidu-meeting-service') {
                    steps {
                        sh "docker run -d -p 8082:8082 --name openvidu-meeting-service -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} openvidu-meeting-service:latest"
                    }
                }
                stage('Docker run openvidu-content-service') {
                    steps {
                        sh "docker run -d -p 8083:8083 --name openvidu-content-service -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} openvidu-content-service:latest"
                    }
                }
                stage('Docker run mattermost-content-service') {
                    steps {
                        sh "docker run -d -p 8084:8084 --name mattermost-content-service -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} mattermost-content-service:latest"
                    }
                }
            }
        }
    }
}
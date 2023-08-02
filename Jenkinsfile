pipeline {
    agent none
    options { skipDefaultCheckout(true) }
    stages {
        stage('Prepare credentials') {
            agent any
            steps {
                withCredentials([
                    file(credentialsId: 'application-dev.properties', variable: 'PROP_FILE'),
                    string(credentialsId: 'gradle.properties', variable: 'GRADLE_PROP')
                ]) {
                    // The credentials can be used within this block
                    sh 'cp $PROP_FILE application-dev.properties' // Copy the secret file to the current directory
                    sh 'echo $GRADLE_PROP > gradle.properties' // Write the secret text to a file
                }
            }
        }
        stage('Build and Test') {
            agent {
                docker {
                    image 'gradle:latest'
                    args '-v /root/.gradle:/root/.gradle'
                }
            }
            options { skipDefaultCheckout(false) }
            steps {
                sh 'cd backend && ./gradlew build'
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


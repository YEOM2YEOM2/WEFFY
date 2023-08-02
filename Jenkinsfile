pipeline {
agent none
options { skipDefaultCheckout(true) }
stages {
stage('Build and Test') {
agent {
docker {
image 'maven:3-alpine'
args '-v /root/.m2:/root/.m2'
}
}
options { skipDefaultCheckout(false) }
steps {
sh 'mvn -B -DskipTests -f pom.xml clean package'
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


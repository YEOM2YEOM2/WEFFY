#FROM openjdk:11-jdk
#VOLUME /tmp
#EXPOSE 8082
#ARG JAR_FILE=build/libs/openvidu-meeting-service-2.28.0.jar
#COPY ${JAR_FILE} app.jar
#
#ENTRYPOINT ["java","-jar","/app.jar"]

# Use Ubuntu as base image
FROM ubuntu:20.04

# Install OpenJDK 11
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install necessary utilities and Gradle 8.2
RUN apt-get update && apt-get install -y wget unzip && \
    wget https://services.gradle.org/distributions/gradle-8.2-bin.zip -P /tmp && \
    unzip -d /opt/gradle /tmp/gradle-*.zip && \
    rm -rf /tmp/* /var/lib/apt/lists/*

ENV GRADLE_HOME=/opt/gradle/gradle-8.2
ENV PATH="$PATH:$GRADLE_HOME/bin"

# Set the working directory
WORKDIR /app

# Copy the Gradle project JAR file
COPY /backend/openvidu-meeting-service/build/libs/openvidu-meeting-service-2.28.0.jar .

# Expose the port the app runs in
EXPOSE 8082

# Run the application JAR
ENTRYPOINT ["java", "-jar", "openvidu-meeting-service-2.28.0.jar"]

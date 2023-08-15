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
    rm -rf /var/lib/apt/lists/*

# Install necessary utilities
RUN apt-get update && apt-get install -y wget unzip

# Set the working directory
WORKDIR /app

# Copy the Gradle project (including the already built artifacts)
COPY backend/openvidu-meeting-service/build/libs/openvidu-meeting-service-2.28.0.jar .

# Expose the port the app runs in and the debug port
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "openvidu-meeting-service-2.28.0.jar"]

#FROM openjdk:11-jdk
#VOLUME /tmp
#EXPOSE 8082
#ARG JAR_FILE=build/libs/openvidu-meeting-service-2.28.0.jar
#COPY ${JAR_FILE} app.jar
#
#ENTRYPOINT ["java","-jar","/app.jar"]

# Use Ubuntu as base image
FROM ubuntu:20.04

# Install necessary packages
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk wget unzip && \
    rm -rf /var/lib/apt/lists/*

# Install Gradle 8.2
RUN wget https://services.gradle.org/distributions/gradle-8.2-bin.zip -P /tmp && \
    unzip -d /opt/gradle /tmp/gradle-*.zip && \
    rm -rf /tmp/*

ENV GRADLE_HOME=/opt/gradle/gradle-8.2
ENV PATH="$PATH:$GRADLE_HOME/bin"

# Set the working directory
WORKDIR /app

# Copy the Gradle project
COPY . .

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the project
RUN ./gradlew clean build --no-daemon -x test

# Expose the port the app runs in
EXPOSE 8082

# Change the working directory to the build directory (optional)
# WORKDIR /app/build/libs

# You should check the exact name of your JAR file after the build
# and replace "openvidu-meeting-service-2.28.0.jar" with the correct name, if necessary.
ENTRYPOINT ["java", "-jar", "/app/build/libs/openvidu-meeting-service-2.28.0.jar"]

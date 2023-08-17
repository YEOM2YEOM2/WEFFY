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

# Install Gradle
RUN wget https://services.gradle.org/distributions/gradle-8.2-bin.zip -P /tmp && \
    unzip -d /opt/gradle /tmp/gradle-*.zip && \
    rm -rf /tmp/*

ENV GRADLE_HOME=/opt/gradle/gradle-9.0
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

# Change the working directory to the build directory
WORKDIR /app/build/libs

# Use the correct JAR filename (replace with the actual filename)
ENTRYPOINT ["java", "-jar", "openvidu-meeting-service-2.28.0.jar"]

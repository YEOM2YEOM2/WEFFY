# Use Ubuntu as base image
FROM ubuntu:20.04

# Update Ubuntu repositories and Install OpenJDK 17
RUN apt-get update -y && apt-get upgrade -y && \
    apt-get install -y openjdk-17-jdk && \
    rm -rf /var/lib/apt/lists/*

# Install Gradle 8.2
RUN apt-get update && apt-get install -y wget unzip \
 && wget https://services.gradle.org/distributions/gradle-8.2-bin.zip -P /tmp \
 && unzip -d /opt/gradle /tmp/gradle-*.zip \
 && rm -rf /tmp/*

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

# Change the working directory to the build directory
WORKDIR /app/build/libs

# Expose the port the app runs in and the debug port
EXPOSE 8081

# Run the application with debug options
CMD ["java", "-jar", "authentication-integration-service-0.0.1-SNAPSHOT.jar"]

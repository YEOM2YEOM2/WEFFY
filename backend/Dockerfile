# Use Ubuntu as base image
FROM ubuntu:20.04

# Install OpenJDK 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the Gradle project
COPY . .

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the project
RUN ./gradlew build --no-daemon -x test

# Change the working directory to the build directory
WORKDIR /app/build/libs

# Expose the port the app runs in
EXPOSE 8081

# Run the application
CMD ["java", "-jar", "weffy-0.0.1-SNAPSHOT.jar"]


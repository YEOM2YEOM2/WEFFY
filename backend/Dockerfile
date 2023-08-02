# Use Ubuntu as base image
FROM ubuntu:20.04

# Install OpenJDK 17 and Gradle
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk gradle && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /var/jenkins_home/workspace/weffy

# Copy the Gradle project
COPY . .

# Build the project
RUN gradle build --no-daemon

# Expose the port the app runs in
EXPOSE 8081

# Run the application
CMD ["java", "-jar", "./build/libs/your-application-name.jar"]

~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
-- INSERT --                                                                                                        1,1           All


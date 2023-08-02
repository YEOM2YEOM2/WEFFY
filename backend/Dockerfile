# Start with a base image containing Java runtime
FROM openjdk:17-jdk-alpine

# Install Gradle
RUN apk add --no-cache gradle

# VOLUME definition for cached data
VOLUME /tmp

# Copy the application's jar file (created via the Gradle build)
COPY ./target/weffy-0.0.1-SNAPSHOT.jar app.jar

# Environment variable for Java options
ENV JAVA_OPTS=""

# The command to run the application
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]


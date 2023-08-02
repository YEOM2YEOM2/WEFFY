FROM gradle:8.2.1-jdk17 AS build

WORKDIR /home/gradle/project

COPY --chown=gradle:gradle . /home/gradle/project

RUN gradle build --no-daemon 

FROM openjdk:17-jdk-alpine

WORKDIR /var/jenkins_home/workspace/weffy

COPY --from=build /home/gradle/project/build/libs/*.jar ./

EXPOSE 8081

CMD ["java", "-jar", "./your-application-name.jar"]


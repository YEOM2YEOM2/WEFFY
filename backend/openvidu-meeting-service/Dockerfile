FROM openjdk:11-jdk
VOLUME /tmp
EXPOSE 8082
ARG JAR_FILE=build/libs/openvidu-meeting-service-2.28.0.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
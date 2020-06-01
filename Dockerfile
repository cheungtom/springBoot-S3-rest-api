FROM openjdk:8-jdk-alpine

MAINTAINER Tom Cheung <cheungtom@hotmail.com>
VOLUME /tmp
VOLUME /target

COPY ./target/app.jar /target/app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/target/app.jar"]

FROM openjdk:11-jre-slim
MAINTAINER "Kyungyoon Kim <ruddbs5302@gmail.com>"
WORKDIR /app

VOLUME /app/log

COPY ./build/libs/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 8080

ENTRYPOINT java \
-Djava.security.egd=file:/dev/./urandom \
-jar /app/app.jar
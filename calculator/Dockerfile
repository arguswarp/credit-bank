
FROM eclipse-temurin:17-alpine

ARG JAR_FILE=calculator-1.0-SNAPSHOT.jar

EXPOSE 8080

ADD target/calculator-1.0-SNAPSHOT.jar app.jar

RUN /bin/sh -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
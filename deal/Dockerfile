FROM eclipse-temurin:17-alpine

ARG JAR_FILE=deal-1.0-SNAPSHOT.jar

ENV DB_URL=jdbc:postgresql://localhost:5432/postgres
ENV DB_USERNAME=admin
ENV DB_PASSWORD=admin

EXPOSE 8081

ADD target/deal-1.0-SNAPSHOT.jar app.jar

RUN /bin/sh -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
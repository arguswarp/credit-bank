FROM eclipse-temurin:17-alpine

ARG JAR_FILE=target/statement-1.0-SNAPSHOT.jar

EXPOSE 8082

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
FROM eclipse-temurin:21 AS builder-0

WORKDIR /app

COPY env.properties ./env.properties
COPY target/*.jar ./app.jar

COPY src/main/resources/images ./src/main/resources/images

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder-0 /app/app.jar ./app.jar
COPY --from=builder-0 /app/env.properties ./env.properties

COPY --from=builder-0 /app/src/main/resources/images ./src/main/resources/images

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]


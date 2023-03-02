# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

COPY . /app

RUN ./gradlew clean build --info

FROM eclipse-temurin:17-jdk-jammy

COPY --from=build /app/build/libs/my-app.jar .

EXPOSE 8080

ENTRYPOINT exec java -jar my-app.jar 
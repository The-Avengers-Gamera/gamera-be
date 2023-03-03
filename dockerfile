# syntax=docker/dockerfile:1

# get the base image
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# copy everything to app and build
COPY . .

ARG SPRING_DATASOURCE_URL
ARG SPRING_DATASOURCE_USERNAME
ARG SPRING_DATASOURCE_PASSWORD
# RUN echo "build datasourse info"
# RUN echo ${SPRING_DATASOURCE_URL} ${SPRING_DATASOURCE_USERNAME} ${SPRING_DATASOURCE_PASSWORD}
ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
ENV SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
ENV SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}

RUN ./gradlew clean build

RUN ./gradlew clean test

# build image
# FROM eclipse-temurin:17-jdk-jammy
# COPY --from=build /app/build/libs/my-app.jar .
EXPOSE 8080
ENTRYPOINT ["./gradlew", "bootRun"]
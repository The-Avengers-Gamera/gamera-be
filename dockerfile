# syntax=docker/dockerfile:1

# get the base image
FROM openjdk:17-alpine

# copy everything to app and build

ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD

# RUN echo "build datasourse info"
# RUN echo ${SPRING_DATASOURCE_URL} ${SPRING_DATASOURCE_USERNAME} ${SPRING_DATASOURCE_PASSWORD}
ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

WORKDIR /app

COPY . .

EXPOSE 8080
ENTRYPOINT ["./gradlew", "bootRun"]
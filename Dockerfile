FROM openjdk:17-jdk-alpine

ARG DB_ENDPOINT
ARG DB_USERNAME
ARG DB_PASSWORD
ARG OPENAI_KEY

ENV DB_ENDPOINT=${DB_ENDPOINT}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV OPENAI_KEY=${OPENAI_KEY}

WORKDIR /app

COPY build /app/build

EXPOSE 8080

CMD ["java", "-jar", "build/libs/gamera-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=uat"]

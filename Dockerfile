FROM openjdk:17-jdk-alpine

ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD

ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

WORKDIR /app

COPY . .

RUN ./gradlew build -Dspring.profiles.active=uat --spring.config.name=application-uat

EXPOSE 8080

CMD ["./gradlew", "bootRun -Dspring.profiles.active=uat --spring.config.name=application-uat"]
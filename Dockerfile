FROM openjdk:17-jdk-alpine

ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

WORKDIR /app

COPY . .

RUN ./gradlew build

EXPOSE 8080

CMD ["./gradlew", "bootRun"]
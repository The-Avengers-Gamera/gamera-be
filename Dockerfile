FROM openjdk:17-jdk-alpine

ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD


ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

# Set the working directory to /app
WORKDIR /app

COPY ..

# Expose port 8080 for the application
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "my-application.jar"]

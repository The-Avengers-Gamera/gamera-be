# Use an openjdk image to build the application 
# doesn't have xargs in the openJDK docker offical image

FROM openjdk:17-jdk-alpine AS build
WORKDIR /app

# Copy the application source code
COPY . /app

# Build the application
RUN ./gradle clean build

# Use a smaller runtime image for the application
FROM openjdk:17-jdk-alpine

# Copy the application JAR file from the build stage
COPY --from=build /app/build/libs/my-app.jar .
EXPOSE 8080

ENTRYPOINT exec java -jar my-app.jar 
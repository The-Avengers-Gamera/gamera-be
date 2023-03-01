# Use an openjdk image to build the application 
# doesn't have xargs in the openJDK docker offical image

FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy the application source code
COPY . /app

# Build the application
RUN ./gradlew clean build --info

# Use a smaller runtime image for the application
FROM eclipse-temurin:17-jdk-jammy

# Copy the application JAR file from the build stage
COPY --from=build /app/build/libs/my-app.jar .
EXPOSE 8080

ENTRYPOINT exec java -jar my-app.jar 
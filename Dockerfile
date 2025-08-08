# Stage 1: Build the application using Gradle
FROM gradle:8.7-jdk17 AS builder
WORKDIR /home/gradle/src
COPY . .
# Build the application, creating a fat JAR
RUN ./gradlew shadowJar --no-daemon

# Stage 2: Create the final, smaller image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copy the built JAR from the builder stage.
# The wildcard *-all.jar makes this robust, finding the JAR regardless of version.
# It is then renamed to app.jar for a consistent entrypoint.
COPY --from=builder /home/gradle/src/build/libs/*-all.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
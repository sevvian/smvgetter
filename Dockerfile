# Stage 1: Build the application using a Gradle container
FROM gradle:8.7-jdk17 AS builder
WORKDIR /home/gradle/src
COPY . .
# Build the fat JAR, skipping tests for faster CI builds
RUN ./gradlew shadowJar --no-daemon

# Stage 2: Create the final, lean image using a minimal JRE
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copy only the built JAR from the builder stage
COPY --from=builder /home/gradle/src/build/libs/app.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
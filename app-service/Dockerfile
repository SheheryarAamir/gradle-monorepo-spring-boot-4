# --- Stage 1: Build Stage (Construction Site) ---
# Using the latest JDK 25 Alpine image available in Jan 2026
FROM eclipse-temurin:25-jdk-alpine AS builder

# Essential for Gradle/Kotlin 2.3 performance on Alpine's musl
RUN apk add --no-cache libc6-compat

WORKDIR /builder

# 1. Copy Gradle wrapper and structure
COPY gradlew .
COPY gradle/ gradle/
COPY build-logic/ build-logic/
COPY settings.gradle.kts .
COPY gradle.properties .
COPY gradle/libs.versions.toml gradle/

# 2. Fix permissions and line endings (Crucial for Windows/Mac to Alpine)
RUN chmod +x gradlew && \
    sed -i 's/\r$//' gradlew

# 3. Copy app-service module
COPY app-service/ app-service/

# 4. Build the JAR using JDK 25
# Note: Ensure your gradle-wrapper is 9.2+ for JDK 25 support
RUN ./gradlew :app-service:bootJar -x test --no-daemon

# --- Stage 2: Tiny Runtime Stage (Gallery) ---
# Switch to the lean JRE 25 to keep the image under 130MB
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Security: Create and use a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built jar from the builder stage
COPY --from=builder /builder/app-service/build/libs/app-service.jar app.jar

# Expose Spring's default port
EXPOSE 8080

# Optimization flags for JDK 25 in containers
ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-jar", "app.jar"]
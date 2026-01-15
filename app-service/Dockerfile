# --- Stage 1: Build ---
FROM eclipse-temurin:25-jdk-alpine AS dependencies
WORKDIR /builder
RUN apk add --no-cache libc6-compat

COPY gradlew .
COPY gradle gradle
COPY build-logic build-logic
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY app-service app-service
COPY common-events common-events

RUN chmod +x gradlew && \
    ./gradlew dependencies --no-daemon || true

# Stage 2 - application changes
FROM dependencies AS builder
WORKDIR /builder

# Now copy source code
COPY app-service app-service

# Build the application
RUN ./gradlew :app-service:bootJar -x test --no-daemon

# --- Stage 3: Extract Layers ---
# This stage breaks the fat JAR into optimized folders
FROM eclipse-temurin:25-jre-alpine AS extractor
WORKDIR /app
COPY --from=builder /builder/app-service/build/libs/app-service.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# --- Stage 4: Runtime ---
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy each layer individually for maximum Docker cache hits
COPY --from=extractor /app/dependencies/ ./
COPY --from=extractor /app/spring-boot-loader/ ./
COPY --from=extractor /app/snapshot-dependencies/ ./
COPY --from=extractor /app/application/ ./

# AOT classes generated in Stage 1 are inside the 'application' layer now
ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "org.springframework.boot.loader.launch.JarLauncher"]
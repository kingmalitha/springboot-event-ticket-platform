# --------------------------
# Stage 1: Build the app
# --------------------------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

# Download dependencies offline
RUN mvn dependency:go-offline -B

COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests

# --------------------------
# Stage 2: Run the app
# --------------------------
FROM openjdk:21-jdk AS runner

WORKDIR /app

# Copy the Spring Boot jar
COPY --from=builder /app/target/springboot-event-ticket-platform-0.0.1-SNAPSHOT.jar ./app.jar

# Copy OpenTelemetry Java Agent from local docker folder
# ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.20.0/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar
COPY docker/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar

# Expose HTTP port
EXPOSE 8080

# Set default environment variables for OTel
ENV OTEL_EXPORTER_OTLP_ENDPOINT=http://lgtm:4318
ENV OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
ENV OTEL_RESOURCE_ATTRIBUTES="service.name=springboot-event-ticket-platform"

# Run the app with the OTel agent
ENTRYPOINT ["java", "-javaagent:/app/opentelemetry-javaagent.jar", "-jar", "app.jar"]

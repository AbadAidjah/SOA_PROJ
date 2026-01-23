FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
RUN chmod +x ./mvnw

# Download dependencies for build cache efficiency
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application (skip tests for speed)
RUN ./mvnw clean package -DskipTests

# Expose the port your prescription service runs on
EXPOSE 8082

# Run the Spring Boot prescription service
CMD ["java", "-jar", "target/prescription-service-0.0.1-SNAPSHOT.jar"]

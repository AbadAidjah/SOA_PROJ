#FROM eclipse-temurin:21-jre
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
# COPY settings.xml /root/.m2/settings.xml
RUN chmod +x ./mvnw

# Download dependencies for build cache efficiency
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application (skip tests for speed)
RUN ./mvnw clean package -DskipTests

# Expose the port your pharmacie service runs on (change if not 8080)
EXPOSE 8080

# Run the Spring Boot pharmacie service
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]

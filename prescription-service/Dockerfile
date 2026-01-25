FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src src

RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B
RUN ./mvnw clean package -DskipTests

EXPOSE 8082

CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
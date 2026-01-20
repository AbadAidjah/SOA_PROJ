FROM openjdk:24-jdk
WORKDIR /app
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn


RUN chmod +x ./mvnw
# RUN chmod +x ./gradlew
# RUN ./gradlew dependencies --no-daemon

RUN ./mvnw dependency:go-offline -B
COPY src src
RUN ./mvnw clean package -DskipTests
# RUN ./gradlew bootJar --no-daemon
# COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]




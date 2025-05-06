# Build stage
FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:21.0.7

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar /UTECare.jar

ENTRYPOINT ["java", "-jar", "/UTECare.jar"]

EXPOSE 8080
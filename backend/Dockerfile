FROM openjdk:21

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} UTECare.jar

ENTRYPOINT ["java", "-jar", "UTECare.jar"]

EXPOSE 8080
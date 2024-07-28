FROM openjdk:17
COPY target/EmployeeManagementSystem.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]


# syntax=docker/dockerfile:1

#FROM eclipse-temurin:17-jdk-jammy
#
#WORKDIR /app
#
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#COPY src ./src
#RUN ./mvnw dependency:resolve
#
#
#CMD ["./mvnw", "spring-boot:run"]

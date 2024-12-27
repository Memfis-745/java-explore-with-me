FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
# FROM amazoncorretto:11-alpine-jdk
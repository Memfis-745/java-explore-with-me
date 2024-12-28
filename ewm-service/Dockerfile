FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/*.jar /app/ewm-service.jar
ENTRYPOINT ["java","-jar","/app/ewm-service.jar"]
# FROM amazoncorretto:11-alpine-jdk
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/*.jar /app/ewm-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/app/ewm-service-0.0.1-SNAPSHOT.jar"]

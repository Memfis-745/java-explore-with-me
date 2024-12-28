FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/*.jar /app/stats-server.jar
ENTRYPOINT ["java","-jar","/app/stats-server.jar"]
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/*.jar /app/stats-server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/app/stats-server-0.0.1-SNAPSHOT.jar"]
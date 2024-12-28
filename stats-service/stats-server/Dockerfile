FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/*.jar /app
ENTRYPOINT ["java","-jar","/stats-server.jar"]
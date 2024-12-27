FROM eclipse-temurin:21-jre-jammy
COPY target/*.jar app.jar
#ENV TZ="Europe/Moscow"
ENTRYPOINT ["java","-jar","/app.jar"]
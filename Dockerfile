FROM amazoncorretto:25.0.1-alpine3.19
WORKDIR /app
COPY target/pdf-generator-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

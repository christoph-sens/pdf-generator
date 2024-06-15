FROM amazoncorretto:21.0.3-alpine3.19
WORKDIR /app
COPY ./target/pdf-generator-0.0.1-SNAPSHOT.jar /app/pdf-generator.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/pdf-generator.jar"]

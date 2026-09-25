FROM gradle:9.7.1-jdk25 AS build
WORKDIR /app
COPY . /app/.
RUN ./gradlew :pdf-generator-app:bootJar --no-daemon

FROM amazoncorretto:25.0.1-alpine3.19
WORKDIR /app
COPY --from=build /app/pdf-generator-app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

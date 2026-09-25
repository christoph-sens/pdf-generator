FROM gradle:9.7.1-jdk25 AS build
WORKDIR /app
COPY . /app/.
RUN ./gradlew :pdf-generator-bootstrap:bootJar --no-daemon

FROM amazoncorretto:25.0.1-alpine3.19
WORKDIR /app
RUN addgroup -S app && adduser -S -G app -H app
COPY --from=build /app/pdf-generator-bootstrap/build/libs/*.jar app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app
RUN groupadd --system app && useradd --system --gid app --no-create-home app
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

FROM gradle:9.7.1-jdk25 AS build
WORKDIR /app
COPY . /app/.
RUN ./gradlew :pdf-generator-bootstrap:bootJar --no-daemon

# Used by CI: takes the boot jar Gradle has already built and tested from the build context "app"
# (docker build --target prebuilt --build-context app=pdf-generator-bootstrap/build/libs .) instead of
# compiling again.
FROM runtime AS prebuilt
COPY --from=app *.jar app.jar

# Default target: self-contained build.
FROM runtime
COPY --from=build /app/pdf-generator-bootstrap/build/libs/*.jar app.jar

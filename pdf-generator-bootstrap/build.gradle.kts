plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

dependencies {
    implementation(platform(libs.spring.boot.bom))

    implementation(project(":pdf-generator-application"))
    implementation(project(":pdf-generator-adapter-web"))
    implementation(project(":pdf-generator-adapter-persistence"))
    implementation(project(":pdf-generator-adapter-rendering"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly(libs.springdoc.webmvc.ui)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.rest.assured.spring.mock.mvc)
    testImplementation(libs.archunit.junit5)
}

// Only the executable boot jar is needed; the plain jar would break the Dockerfile's *.jar glob.
tasks.jar {
    enabled = false
}

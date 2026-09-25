plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(platform(libs.spring.boot.bom))

    implementation(project(":pdf-generator-application"))

    implementation("org.springframework:spring-context")
    implementation("org.thymeleaf:thymeleaf-spring6")
    implementation(libs.flying.saucer.pdf)

    testImplementation(libs.kotlin.test.junit5)
}

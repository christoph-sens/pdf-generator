plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(platform(libs.spring.boot.bom))

    implementation(project(":pdf-generator-application"))
    implementation(libs.kotlin.reflect)

    implementation(libs.spring.context)
    implementation(libs.thymeleaf.spring6)
    implementation(libs.flying.saucer.pdf)

    testImplementation(libs.kotlin.test.junit5)
}

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(platform(libs.spring.boot.bom))

    implementation(project(":pdf-generator-application"))
    implementation(libs.kotlin.reflect)

    implementation(libs.spring.boot.starter.web)
    implementation(libs.commons.csv)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.starter.webmvc.test)
    testImplementation(libs.kotlin.test.junit5)
}

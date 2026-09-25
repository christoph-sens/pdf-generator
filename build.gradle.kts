buildscript {
    dependencies {
        constraints {
            // Spring Boot's Gradle plugin (buildpack image support) pulls commons-lang3 3.16.0, affected by
            // GHSA-j288-q9x7-2f5v. Build-time only, but keep the build classpath clean.
            classpath("org.apache.commons:commons-lang3:3.20.0")
        }
    }
}

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.jpa) apply false
    alias(libs.plugins.spring.boot) apply false
}

allprojects {
    group = "de.christophsens"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    // Spring Boot 4.1.1 still manages Tomcat 11.0.24 (GHSA-h3x4-894j-xpx5, GHSA-9xv2-5v5q-p794,
    // GHSA-gcx9-497g-6cp6). Drop this once the Boot BOM ships Tomcat >= 11.0.25.
    configurations.configureEach {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.apache.tomcat.embed") {
                useVersion(rootProject.libs.versions.tomcat.get())
                because("Spring Boot's managed Tomcat version has known critical vulnerabilities")
            }
        }
    }

    plugins.withId("org.jetbrains.kotlin.jvm") {
        extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
            jvmToolchain(rootProject.libs.versions.java.get().toInt())
            compilerOptions {
                freeCompilerArgs.add("-Xjsr305=strict")
            }
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

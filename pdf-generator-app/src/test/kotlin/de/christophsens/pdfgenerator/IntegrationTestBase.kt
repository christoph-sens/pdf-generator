@file:Suppress("DEPRECATION")
package de.christophsens.pdfgenerator

import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
abstract class IntegrationTestBase {
    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val postgresql: PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:16.3"))
    }
}
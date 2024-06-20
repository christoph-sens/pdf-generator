package de.christophsens.pdfgenerator

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName

abstract class IntegrationTestBase {
    companion object {
        @Container
        @ServiceConnection
        val postgresql = PostgreSQLContainer(DockerImageName.parse("postgres:latest"))

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            postgresql.start()
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            postgresql.stop()
        }
    }
}
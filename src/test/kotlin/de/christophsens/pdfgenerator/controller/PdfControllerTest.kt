package de.christophsens.pdfgenerator.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.christophsens.pdfgenerator.controller.dto.Item
import de.christophsens.pdfgenerator.controller.dto.Order
import de.christophsens.pdfgenerator.repository.TemplateRepository
import de.christophsens.pdfgenerator.repository.TranslationRepository
import de.christophsens.pdfgenerator.service.TemplateService
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class PdfControllerTest {

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

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var templateService: TemplateService

    @Autowired
    lateinit var translationRepository: TranslationRepository

    @Autowired
    lateinit var templateRepository: TemplateRepository

    @BeforeEach
    fun setUp() {
        translationRepository.deleteAll()
        templateRepository.deleteAll()
        mockMvc(mockMvc)
    }

    @Test
    fun `test addTemplate and addTranslations and generatePdf`() {
        // add a template to the database
        val template = """<html><body><h1 th:text="${'$'}{translation.name}"></h1></body></html>"""
        val name = "templateName"
        val countryCode = "US"

        RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .body(template)
            .pathParam("name", name)
            .pathParam("countryCode", countryCode)
            .put("/template/{name}/{countryCode}")
            .then()
            .statusCode(200)

        // check if the template has been added to the database
        val templateEntity = templateService.getTemplate(name, countryCode)
        Assertions.assertEquals(template, templateEntity.content)

        // add the translations to the database
        val fileContent = """
           name,value
        """.trimIndent()

        val templateName = "templateName"
        val languageCode = "en"

        RestAssuredMockMvc.given()
            .contentType(ContentType.MULTIPART)
            .multiPart("file", fileContent)
            .put("/translations/$templateName/$countryCode/$languageCode")
            .then()
            .statusCode(200)

        // check if the translations are available and related to the specified template
        val templateDto = templateService.getTemplate(templateName, countryCode)
        assertEquals(templateName, templateDto.name)

        // check if the response
        val responseBody = RestAssuredMockMvc.given()
            .body(emptyMap<String, Any>())
            .post("/pdf/$name/$countryCode/$languageCode")
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_PDF_VALUE)
            .extract().body()
        assertTrue(responseBody.asByteArray().isNotEmpty())
    }

    @Test
    fun `generate invoice`() {
        val templateName = "invoice"
        val templateContent = getInvoiceTemplate()
        val countryCode = "DE"

        RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .body(templateContent)
            .pathParam("name", templateName)
            .pathParam("countryCode", countryCode)
            .put("/template/{name}/{countryCode}")
            .then()
            .statusCode(200)

        val translations = getTranslationsDe()
        val languageCode = "de"
        RestAssuredMockMvc.given()
            .contentType(ContentType.MULTIPART)
            .multiPart("file", translations)
            .put("/translations/$templateName/$countryCode/$languageCode")
            .then()
            .statusCode(200)
        val data = getOrderTestData()
        val jsonString = ObjectMapper().writeValueAsString(data)
        val responseBody = RestAssuredMockMvc.given()
            .body(jsonString)
            .post("/pdf/$templateName/$countryCode/$languageCode")
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_PDF_VALUE)
            .extract().body()
        val file = File("template.pdf")
        file.writeBytes(responseBody.asByteArray())
    }


    fun getOrderTestData(): Order {
        val itemList = listOf(
            Item("item 1", 1, 1.00, 19.00),
            Item("item 2", 2, 2.00, 19.00),
            Item("item 3", 3, 3.00, 19.00),
            Item("item 4", 4, 4.00, 19.00)
        )
        return Order(itemList, 30.00, "Euro")
    }

    fun getTranslationsDe(): String {
        return """
            title,Rechnung
            item,Artikel
            price,Preis
            netto,Netto
            tax,MwSt.
            brutto,Brutto
            brutto2,Total:
            quantity,Anzahl
        """.trimIndent()
    }

    fun getInvoiceTemplate(): String {
        val path = "src/main/resources/templates/invoice.html"
        return File(path).readText(Charsets.UTF_8)
    }
}
package de.christophsens.pdfgenerator.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.christophsens.pdfgenerator.IntegrationTestBase
import de.christophsens.pdfgenerator.adapter.outbound.persistence.repository.SpringDataTemplateRepository
import de.christophsens.pdfgenerator.adapter.outbound.persistence.repository.SpringDataTranslationRepository
import de.christophsens.pdfgenerator.controller.dto.Item
import de.christophsens.pdfgenerator.controller.dto.Order
import de.christophsens.pdfgenerator.domain.port.inbound.ManageTemplateUseCase
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class PdfControllerTest : IntegrationTestBase() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var manageTemplateUseCase: ManageTemplateUseCase

    @Autowired
    lateinit var springDataTranslationRepository: SpringDataTranslationRepository

    @Autowired
    lateinit var springDataTemplateRepository: SpringDataTemplateRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        springDataTranslationRepository.deleteAll()
        springDataTemplateRepository.deleteAll()
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
        val templateEntity = manageTemplateUseCase.getTemplate(name, countryCode)
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
        val template2 = manageTemplateUseCase.getTemplate(templateName, countryCode)
        assertEquals(templateName, template2.name)

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
        val jsonString = objectMapper.writeValueAsString(data)
        val responseBody = RestAssuredMockMvc.given()
            .body(jsonString)
            .post("/pdf/$templateName/$countryCode/$languageCode")
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_PDF_VALUE)
            .extract().body()
        assertTrue(responseBody.asByteArray().isNotEmpty())
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
        return this::class.java.classLoader.getResourceAsStream("templates/invoice.html")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: throw IllegalStateException("Could not find invoice.html in classpath")
    }
}


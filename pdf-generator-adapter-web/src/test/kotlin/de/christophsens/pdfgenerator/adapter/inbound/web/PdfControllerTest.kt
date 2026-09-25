package de.christophsens.pdfgenerator.adapter.inbound.web

import de.christophsens.pdfgenerator.application.port.inbound.GeneratePdfCommand
import de.christophsens.pdfgenerator.application.port.inbound.GeneratePdfUseCase
import de.christophsens.pdfgenerator.application.port.inbound.ManageTemplateUseCase
import de.christophsens.pdfgenerator.application.port.inbound.ManageTranslationsUseCase
import de.christophsens.pdfgenerator.application.port.inbound.SaveTemplateCommand
import de.christophsens.pdfgenerator.application.port.inbound.SaveTranslationsCommand
import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import de.christophsens.pdfgenerator.domain.model.CountryCode
import de.christophsens.pdfgenerator.domain.model.LanguageCode
import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.model.TemplateKey
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.assertEquals

@WebMvcTest(PdfController::class)
@Import(PdfControllerTest.FakeUseCaseConfiguration::class)
class PdfControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var useCases: FakeUseCases

    private val invoiceDe = TemplateKey("invoice", CountryCode("DE"))

    @BeforeEach
    fun reset() {
        useCases.reset()
    }

    @Test
    fun `PUT template passes key and content to the use case`() {
        mockMvc.perform(put("/template/invoice/DE").content("<html/>"))
            .andExpect(status().isOk)

        assertEquals(SaveTemplateCommand(invoiceDe, "<html/>"), useCases.savedTemplate)
    }

    @Test
    fun `PUT translations parses the uploaded CSV`() {
        val file = MockMultipartFile("file", "de.csv", "text/csv", "title,Rechnung\nnote,\"a, b\"".toByteArray())

        mockMvc.perform(multipart("/translations/invoice/DE/de").file(file).with { it.method = "PUT"; it })
            .andExpect(status().isOk)

        assertEquals(
            SaveTranslationsCommand(invoiceDe, LanguageCode("de"), mapOf("title" to "Rechnung", "note" to "a, b")),
            useCases.savedTranslations
        )
    }

    @Test
    fun `PUT translations rejects an empty file`() {
        val file = MockMultipartFile("file", ByteArray(0))

        mockMvc.perform(multipart("/translations/invoice/DE/de").file(file).with { it.method = "PUT"; it })
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST pdf accepts JSON without content type and returns the PDF`() {
        mockMvc.perform(post("/pdf/invoice/DE/de").content("""{"total": 30}"""))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_PDF))
            .andExpect(content().bytes("%PDF".toByteArray()))

        assertEquals(GeneratePdfCommand(invoiceDe, LanguageCode("de"), mapOf("total" to 30)), useCases.generated)
    }

    @Test
    fun `unknown template yields 404`() {
        mockMvc.perform(post("/pdf/missing/DE/de").content("{}"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `invalid codes and malformed JSON yield 400`() {
        mockMvc.perform(put("/template/invoice/DEU").content("<html/>")).andExpect(status().isBadRequest)
        mockMvc.perform(post("/pdf/invoice/DE/deu").content("{}")).andExpect(status().isBadRequest)
        mockMvc.perform(post("/pdf/invoice/DE/de").content("not json")).andExpect(status().isBadRequest)
        mockMvc.perform(post("/pdf/invoice/DE/de").content("[1, 2]")).andExpect(status().isBadRequest)
    }

    @TestConfiguration
    class FakeUseCaseConfiguration {
        /** One fake implementing all inbound ports; the controller gets it injected for each of them. */
        @Bean
        fun fakeUseCases() = FakeUseCases()
    }

    class FakeUseCases : ManageTemplateUseCase, ManageTranslationsUseCase, GeneratePdfUseCase {
        var savedTemplate: SaveTemplateCommand? = null
        var savedTranslations: SaveTranslationsCommand? = null
        var generated: GeneratePdfCommand? = null

        fun reset() {
            savedTemplate = null
            savedTranslations = null
            generated = null
        }

        override fun saveOrUpdate(command: SaveTemplateCommand) {
            savedTemplate = command
        }

        override fun getTemplate(key: TemplateKey): Template = throw TemplateNotFoundException(key)

        override fun saveTranslations(command: SaveTranslationsCommand) {
            savedTranslations = command
        }

        override fun generate(command: GeneratePdfCommand): ByteArray {
            if (command.templateKey.name == "missing") throw TemplateNotFoundException(command.templateKey)
            generated = command
            return "%PDF".toByteArray()
        }
    }
}

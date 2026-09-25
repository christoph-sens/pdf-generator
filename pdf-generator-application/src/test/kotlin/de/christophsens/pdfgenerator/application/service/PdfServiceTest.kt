package de.christophsens.pdfgenerator.application.service

import de.christophsens.pdfgenerator.application.port.inbound.GeneratePdfCommand
import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.model.Translation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PdfServiceTest {

    private val templateRepository = InMemoryTemplateRepository()
    private val htmlRenderer = RecordingHtmlRenderer()
    private val service = PdfService(templateRepository, htmlRenderer, EchoPdfRenderer())

    @Test
    fun `generate renders only translations of the requested language`() {
        templateRepository.save(
            Template(
                key = INVOICE_DE,
                content = "body",
                translations = listOf(
                    Translation(name = "title", value = "Rechnung", languageCode = DE),
                    Translation(name = "title", value = "Invoice", languageCode = EN)
                )
            )
        )

        val pdf = service.generate(GeneratePdfCommand(INVOICE_DE, DE, mapOf("total" to 30)))

        assertEquals("<html>body</html>", pdf.decodeToString())
        val request = htmlRenderer.lastRequest!!
        assertEquals(mapOf("title" to "Rechnung"), request.translations)
        assertEquals(mapOf("total" to 30), request.data)
    }

    @Test
    fun `generate throws TemplateNotFoundException for unknown template`() {
        assertFailsWith<TemplateNotFoundException> {
            service.generate(GeneratePdfCommand(INVOICE_DE, DE, emptyMap()))
        }
    }
}

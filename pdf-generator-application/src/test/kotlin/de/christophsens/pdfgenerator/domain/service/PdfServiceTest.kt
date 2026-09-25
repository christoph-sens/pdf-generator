package de.christophsens.pdfgenerator.domain.service

import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.model.Translation
import kotlin.test.Test
import kotlin.test.assertEquals

class PdfServiceTest {

    private val templateRepository = InMemoryTemplateRepository()
    private val htmlRenderer = RecordingHtmlRenderer()
    private val service = PdfService(templateRepository, htmlRenderer, EchoPdfRenderer())

    @Test
    fun `generate renders only translations of the requested language`() {
        templateRepository.save(
            Template(
                name = "invoice",
                countryCode = "DE",
                content = "",
                translations = listOf(
                    Translation(name = "title", value = "Rechnung", languageCode = "de"),
                    Translation(name = "title", value = "Invoice", languageCode = "en")
                )
            )
        )

        val pdf = service.generate("invoice", "DE", "de", mapOf("total" to 30))

        assertEquals("<html>invoice</html>", pdf.decodeToString())
        assertEquals(listOf("Rechnung"), htmlRenderer.lastTemplate!!.translations.map { it.value })
        assertEquals(mapOf("total" to 30), htmlRenderer.lastData)
    }
}

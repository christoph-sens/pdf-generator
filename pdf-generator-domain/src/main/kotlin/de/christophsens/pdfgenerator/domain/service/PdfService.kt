package de.christophsens.pdfgenerator.domain.service

import de.christophsens.pdfgenerator.domain.port.inbound.GeneratePdfUseCase
import de.christophsens.pdfgenerator.domain.port.outbound.HtmlRenderer
import de.christophsens.pdfgenerator.domain.port.outbound.PdfRenderer
import de.christophsens.pdfgenerator.domain.port.outbound.TemplateRepository

class PdfService(
    private val templateRepository: TemplateRepository,
    private val htmlRenderer: HtmlRenderer,
    private val pdfRenderer: PdfRenderer
) : GeneratePdfUseCase {

    override fun generate(
        templateName: String,
        countryCode: String,
        languageCode: String,
        data: Map<String, Any>
    ): ByteArray {
        // Load template
        val template = templateRepository.findByNameAndCountryCode(templateName, countryCode)
            ?: throw IllegalArgumentException("Template not found")

        // Filter translations for the given language
        val filteredTemplate = template.copy(
            translations = template.translations.filter { it.languageCode == languageCode }
        )

        // Render HTML
        val html = htmlRenderer.render(filteredTemplate, data)

        // Render PDF
        return pdfRenderer.renderFromHtml(html)
    }
}


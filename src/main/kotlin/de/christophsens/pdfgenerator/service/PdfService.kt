package de.christophsens.pdfgenerator.service

import de.christophsens.pdfgenerator.util.generatePdfFromHtml
import org.springframework.stereotype.Service

@Service
class PdfService(
    private val templateService: TemplateService,
    private val htmlGenerationService: HtmlGenerationService
) {
    fun getPdf(name: String, countryCode: String, languageCode: String, data: Map<String, Any>): ByteArray {
        // get template and translations
        val templateDto = templateService.getTemplate(name, countryCode)
        // generate html
        val html = htmlGenerationService.generateHtml(templateDto, data)
        // generate PDF and return it
        return generatePdfFromHtml(html)
    }
}
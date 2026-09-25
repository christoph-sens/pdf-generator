package de.christophsens.pdfgenerator.application.service

import de.christophsens.pdfgenerator.application.port.inbound.GeneratePdfCommand
import de.christophsens.pdfgenerator.application.port.inbound.GeneratePdfUseCase
import de.christophsens.pdfgenerator.application.port.outbound.HtmlRenderer
import de.christophsens.pdfgenerator.application.port.outbound.PdfRenderer
import de.christophsens.pdfgenerator.application.port.outbound.RenderRequest
import de.christophsens.pdfgenerator.application.port.outbound.TemplateRepository
import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException

class PdfService(
    private val templateRepository: TemplateRepository,
    private val htmlRenderer: HtmlRenderer,
    private val pdfRenderer: PdfRenderer
) : GeneratePdfUseCase {

    override fun generate(command: GeneratePdfCommand): ByteArray {
        val template = templateRepository.findByKey(command.templateKey)
            ?: throw TemplateNotFoundException(command.templateKey)

        val html = htmlRenderer.render(
            RenderRequest(
                templateContent = template.content,
                translations = template.translationsFor(command.languageCode),
                data = command.data
            )
        )

        return pdfRenderer.renderFromHtml(html)
    }
}

package de.christophsens.pdfgenerator.application.port.inbound

import de.christophsens.pdfgenerator.domain.model.LanguageCode
import de.christophsens.pdfgenerator.domain.model.TemplateKey

interface GeneratePdfUseCase {
    fun generate(command: GeneratePdfCommand): ByteArray
}

data class GeneratePdfCommand(
    val templateKey: TemplateKey,
    val languageCode: LanguageCode,
    val data: Map<String, Any?>
)

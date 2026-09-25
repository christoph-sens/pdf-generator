package de.christophsens.pdfgenerator.application.port.inbound

import de.christophsens.pdfgenerator.domain.model.LanguageCode
import de.christophsens.pdfgenerator.domain.model.TemplateKey

interface ManageTranslationsUseCase {
    fun saveTranslations(command: SaveTranslationsCommand)
}

data class SaveTranslationsCommand(
    val templateKey: TemplateKey,
    val languageCode: LanguageCode,
    val csvContent: String
)

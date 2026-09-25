package de.christophsens.pdfgenerator.application.port.outbound

import de.christophsens.pdfgenerator.domain.model.LanguageCode
import de.christophsens.pdfgenerator.domain.model.TemplateKey
import de.christophsens.pdfgenerator.domain.model.Translation

interface TranslationRepository {
    fun replaceAll(templateKey: TemplateKey, languageCode: LanguageCode, translations: List<Translation>)
}

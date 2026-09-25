package de.christophsens.pdfgenerator.application.port.outbound

import de.christophsens.pdfgenerator.domain.model.TemplateKey
import de.christophsens.pdfgenerator.domain.model.Translation

interface TranslationRepository {
    fun saveAll(templateKey: TemplateKey, translations: List<Translation>)
    fun deleteAll()
}

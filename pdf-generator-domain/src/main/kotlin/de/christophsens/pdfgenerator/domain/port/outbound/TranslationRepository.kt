package de.christophsens.pdfgenerator.domain.port.outbound

import de.christophsens.pdfgenerator.domain.model.Translation

interface TranslationRepository {
    fun saveAll(translations: List<Translation>, templateName: String, countryCode: String)
    fun deleteAll()
}

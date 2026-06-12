package de.christophsens.pdfgenerator.domain.port.inbound

interface ManageTranslationsUseCase {
    fun saveTranslations(templateName: String, countryCode: String, languageCode: String, csvContent: String)
}

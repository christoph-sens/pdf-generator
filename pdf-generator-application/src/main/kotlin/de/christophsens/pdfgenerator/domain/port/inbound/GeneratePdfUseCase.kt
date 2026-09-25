package de.christophsens.pdfgenerator.domain.port.inbound

interface GeneratePdfUseCase {
    fun generate(templateName: String, countryCode: String, languageCode: String, data: Map<String, Any>): ByteArray
}

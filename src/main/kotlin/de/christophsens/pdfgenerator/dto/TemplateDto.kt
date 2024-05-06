package de.christophsens.pdfgenerator.dto

data class TemplateDto(
    var id: Long?,
    var name: String?,
    var countryCode: String?,
    var content: String?,
    var translations: List<TranslationDto>?
)

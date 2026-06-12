package de.christophsens.pdfgenerator.domain.model

data class Template(
    val id: Long? = null,
    val name: String,
    val countryCode: String,
    val content: String,
    val translations: List<Translation> = emptyList()
)

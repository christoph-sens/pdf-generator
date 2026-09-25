package de.christophsens.pdfgenerator.domain.model

data class Translation(
    val id: Long? = null,
    val name: String,
    val value: String,
    val languageCode: String
)

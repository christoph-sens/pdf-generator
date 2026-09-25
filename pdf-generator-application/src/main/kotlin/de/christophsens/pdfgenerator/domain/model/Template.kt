package de.christophsens.pdfgenerator.domain.model

data class Template(
    val id: Long? = null,
    val key: TemplateKey,
    val content: String,
    val translations: List<Translation> = emptyList()
) {
    fun translationsFor(languageCode: LanguageCode): Map<String, String> =
        translations.filter { it.languageCode == languageCode }.associate { it.name to it.value }
}

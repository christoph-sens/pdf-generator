package de.christophsens.pdfgenerator.domain.service

import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import de.christophsens.pdfgenerator.domain.model.Translation
import de.christophsens.pdfgenerator.domain.port.inbound.ManageTranslationsUseCase
import de.christophsens.pdfgenerator.domain.port.outbound.TemplateRepository
import de.christophsens.pdfgenerator.domain.port.outbound.TranslationRepository

class TranslationService(
    private val translationRepository: TranslationRepository,
    private val templateRepository: TemplateRepository
) : ManageTranslationsUseCase {

    override fun saveTranslations(
        templateName: String,
        countryCode: String,
        languageCode: String,
        csvContent: String
    ) {
        // Verify template exists
        templateRepository.findByNameAndCountryCode(templateName, countryCode)
            ?: throw TemplateNotFoundException("Template not found: $templateName for country $countryCode")

        // Parse CSV content
        val translationMap = parseCsv(csvContent)

        // Create Translation domain models
        val translations = translationMap.map { (name, value) ->
            Translation(name = name, value = value, languageCode = languageCode)
        }

        translationRepository.saveAll(translations, templateName, countryCode)
    }

    private fun parseCsv(csvContent: String): Map<String, String> {
        return csvContent.split("\n")
            .filter { it.isNotBlank() }
            .associate { line ->
                val parts = line.split(",").map { it.trim() }
                if (parts.size >= 2) {
                    parts[0] to parts[1]
                } else {
                    parts[0] to ""
                }
            }
    }
}





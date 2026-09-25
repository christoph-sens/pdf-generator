package de.christophsens.pdfgenerator.application.service

import de.christophsens.pdfgenerator.application.port.inbound.ManageTranslationsUseCase
import de.christophsens.pdfgenerator.application.port.inbound.SaveTranslationsCommand
import de.christophsens.pdfgenerator.application.port.outbound.TemplateRepository
import de.christophsens.pdfgenerator.application.port.outbound.TranslationRepository
import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import de.christophsens.pdfgenerator.domain.model.Translation

class TranslationService(
    private val translationRepository: TranslationRepository,
    private val templateRepository: TemplateRepository
) : ManageTranslationsUseCase {

    override fun saveTranslations(command: SaveTranslationsCommand) {
        templateRepository.findByKey(command.templateKey) ?: throw TemplateNotFoundException(command.templateKey)

        val translations = parseCsv(command.csvContent).map { (name, value) ->
            Translation(name = name, value = value, languageCode = command.languageCode)
        }

        translationRepository.saveAll(command.templateKey, translations)
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

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

        val translations = command.translations.map { (name, value) ->
            Translation(name = name, value = value, languageCode = command.languageCode)
        }

        translationRepository.replaceAll(command.templateKey, command.languageCode, translations)
    }
}

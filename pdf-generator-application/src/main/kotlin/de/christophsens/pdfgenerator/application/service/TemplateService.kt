package de.christophsens.pdfgenerator.application.service

import de.christophsens.pdfgenerator.application.port.inbound.ManageTemplateUseCase
import de.christophsens.pdfgenerator.application.port.inbound.SaveTemplateCommand
import de.christophsens.pdfgenerator.application.port.outbound.TemplateRepository
import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.model.TemplateKey

class TemplateService(
    private val templateRepository: TemplateRepository
) : ManageTemplateUseCase {

    override fun saveOrUpdate(command: SaveTemplateCommand) {
        val existingTemplate = templateRepository.findByKey(command.templateKey)
        val template = existingTemplate?.copy(content = command.content)
            ?: Template(key = command.templateKey, content = command.content)
        templateRepository.save(template)
    }

    override fun getTemplate(key: TemplateKey): Template =
        templateRepository.findByKey(key) ?: throw TemplateNotFoundException(key)
}

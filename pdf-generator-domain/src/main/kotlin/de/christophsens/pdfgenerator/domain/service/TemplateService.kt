package de.christophsens.pdfgenerator.domain.service

import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.port.inbound.ManageTemplateUseCase
import de.christophsens.pdfgenerator.domain.port.outbound.TemplateRepository

class TemplateService(
    private val templateRepository: TemplateRepository
) : ManageTemplateUseCase {

    override fun saveOrUpdate(name: String, countryCode: String, content: String) {
        val existingTemplate = templateRepository.findByNameAndCountryCode(name, countryCode)
        val template = if (existingTemplate != null) {
            existingTemplate.copy(content = content)
        } else {
            Template(name = name, countryCode = countryCode, content = content)
        }
        templateRepository.save(template)
    }

    override fun getTemplate(name: String, countryCode: String): Template {
        return templateRepository.findByNameAndCountryCode(name, countryCode)
            ?: throw TemplateNotFoundException("Template with name=$name and countryCode=$countryCode not found")
    }
}


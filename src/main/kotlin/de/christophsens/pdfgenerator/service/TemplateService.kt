package de.christophsens.pdfgenerator.service

import de.christophsens.pdfgenerator.dto.TemplateDto
import de.christophsens.pdfgenerator.entity.TemplateEntity
import de.christophsens.pdfgenerator.repository.TemplateRepository
import de.christophsens.pdfgenerator.util.mapTemplateEntityToDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TemplateService(private val templateRepository: TemplateRepository) {

    fun saveOrUpdate(templateEntity: TemplateEntity): TemplateEntity {
        val entityList =
            templateRepository.findByNameAndCountryCode(templateEntity.name, templateEntity.countryCode)
        if(entityList.isEmpty()) {
            return templateRepository.save(templateEntity)
        }
        templateEntity.id = entityList.first().id
        return templateRepository.save(templateEntity)

    }
    @Transactional
    fun getTemplate(name: String, countryCode: String): TemplateDto {
        return mapTemplateEntityToDTO(templateRepository.findByNameAndCountryCode(name, countryCode).first())
    }
}
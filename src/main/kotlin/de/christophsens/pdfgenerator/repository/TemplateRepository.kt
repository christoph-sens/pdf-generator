package de.christophsens.pdfgenerator.repository

import de.christophsens.pdfgenerator.entity.TemplateEntity
import org.springframework.data.repository.CrudRepository

interface TemplateRepository : CrudRepository<TemplateEntity, Long> {
    fun findByNameAndCountryCode(name: String?, countryCode: String?): List<TemplateEntity>
}
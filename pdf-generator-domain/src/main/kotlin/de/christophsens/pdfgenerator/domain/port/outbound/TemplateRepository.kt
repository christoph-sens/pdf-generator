package de.christophsens.pdfgenerator.domain.port.outbound

import de.christophsens.pdfgenerator.domain.model.Template

interface TemplateRepository {
    fun findByNameAndCountryCode(name: String, countryCode: String): Template?
    fun save(template: Template): Template
}

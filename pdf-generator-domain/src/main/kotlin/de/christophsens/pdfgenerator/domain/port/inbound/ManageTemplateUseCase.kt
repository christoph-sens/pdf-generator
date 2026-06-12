package de.christophsens.pdfgenerator.domain.port.inbound

import de.christophsens.pdfgenerator.domain.model.Template

interface ManageTemplateUseCase {
    fun saveOrUpdate(name: String, countryCode: String, content: String)
    fun getTemplate(name: String, countryCode: String): Template
}

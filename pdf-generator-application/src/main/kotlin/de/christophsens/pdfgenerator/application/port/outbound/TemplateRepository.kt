package de.christophsens.pdfgenerator.application.port.outbound

import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.model.TemplateKey

interface TemplateRepository {
    fun findByKey(key: TemplateKey): Template?
    fun save(template: Template): Template
}

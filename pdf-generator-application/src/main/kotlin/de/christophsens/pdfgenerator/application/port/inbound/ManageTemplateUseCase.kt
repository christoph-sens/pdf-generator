package de.christophsens.pdfgenerator.application.port.inbound

import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.model.TemplateKey

interface ManageTemplateUseCase {
    fun saveOrUpdate(command: SaveTemplateCommand)
    fun getTemplate(key: TemplateKey): Template
}

data class SaveTemplateCommand(val templateKey: TemplateKey, val content: String)

package de.christophsens.pdfgenerator.domain.port.outbound

import de.christophsens.pdfgenerator.domain.model.Template

interface HtmlRenderer {
    fun render(template: Template, data: Any): String
}

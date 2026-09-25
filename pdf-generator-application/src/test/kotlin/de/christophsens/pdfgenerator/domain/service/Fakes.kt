package de.christophsens.pdfgenerator.domain.service

import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.model.Translation
import de.christophsens.pdfgenerator.domain.port.outbound.HtmlRenderer
import de.christophsens.pdfgenerator.domain.port.outbound.PdfRenderer
import de.christophsens.pdfgenerator.domain.port.outbound.TemplateRepository
import de.christophsens.pdfgenerator.domain.port.outbound.TranslationRepository

class InMemoryTemplateRepository : TemplateRepository {
    val templates = mutableListOf<Template>()

    override fun findByNameAndCountryCode(name: String, countryCode: String): Template? =
        templates.find { it.name == name && it.countryCode == countryCode }

    override fun save(template: Template): Template {
        templates.removeIf { it.name == template.name && it.countryCode == template.countryCode }
        templates += template
        return template
    }
}

class InMemoryTranslationRepository : TranslationRepository {
    val saved = mutableListOf<Translation>()

    override fun saveAll(translations: List<Translation>, templateName: String, countryCode: String) {
        saved += translations
    }

    override fun deleteAll() {
        saved.clear()
    }
}

class RecordingHtmlRenderer : HtmlRenderer {
    var lastTemplate: Template? = null
    var lastData: Any? = null

    override fun render(template: Template, data: Any): String {
        lastTemplate = template
        lastData = data
        return "<html>${template.name}</html>"
    }
}

class EchoPdfRenderer : PdfRenderer {
    override fun renderFromHtml(html: String): ByteArray = html.toByteArray()
}

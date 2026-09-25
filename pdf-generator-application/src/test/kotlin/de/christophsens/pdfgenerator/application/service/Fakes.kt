package de.christophsens.pdfgenerator.application.service

import de.christophsens.pdfgenerator.application.port.outbound.HtmlRenderer
import de.christophsens.pdfgenerator.application.port.outbound.PdfRenderer
import de.christophsens.pdfgenerator.application.port.outbound.RenderRequest
import de.christophsens.pdfgenerator.application.port.outbound.TemplateRepository
import de.christophsens.pdfgenerator.application.port.outbound.TranslationRepository
import de.christophsens.pdfgenerator.domain.model.CountryCode
import de.christophsens.pdfgenerator.domain.model.LanguageCode
import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.model.TemplateKey
import de.christophsens.pdfgenerator.domain.model.Translation

val INVOICE_DE = TemplateKey("invoice", CountryCode("DE"))
val DE = LanguageCode("de")
val EN = LanguageCode("en")

class InMemoryTemplateRepository : TemplateRepository {
    val templates = mutableMapOf<TemplateKey, Template>()

    override fun findByKey(key: TemplateKey): Template? = templates[key]

    override fun save(template: Template): Template {
        templates[template.key] = template
        return template
    }
}

class InMemoryTranslationRepository : TranslationRepository {
    val saved = mutableMapOf<TemplateKey, MutableList<Translation>>()

    override fun saveAll(templateKey: TemplateKey, translations: List<Translation>) {
        saved.getOrPut(templateKey) { mutableListOf() } += translations
    }

    override fun deleteAll() {
        saved.clear()
    }
}

class RecordingHtmlRenderer : HtmlRenderer {
    var lastRequest: RenderRequest? = null

    override fun render(request: RenderRequest): String {
        lastRequest = request
        return "<html>${request.templateContent}</html>"
    }
}

class EchoPdfRenderer : PdfRenderer {
    override fun renderFromHtml(html: String): ByteArray = html.toByteArray()
}

package de.christophsens.pdfgenerator.adapter.outbound.html

import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.port.outbound.HtmlRenderer
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.StringTemplateResolver

@Component
class ThymeleafHtmlRenderer(private val templateEngine: TemplateEngine) : HtmlRenderer {

    init {
        templateEngine.setTemplateResolver(StringTemplateResolver())
    }

    override fun render(template: Template, data: Any): String {
        // Transform translations to map
        val translationMap: Map<String?, String?> =
            template.translations.associate { it.name to it.value }

        // Set context
        val context = Context().apply {
            setVariable("translation", translationMap)
            setVariable("data", data)
        }

        return templateEngine.process(template.content, context)
    }
}


package de.christophsens.pdfgenerator.adapter.outbound.html

import de.christophsens.pdfgenerator.application.port.outbound.HtmlRenderer
import de.christophsens.pdfgenerator.application.port.outbound.RenderRequest
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.StringTemplateResolver

@Component
class ThymeleafHtmlRenderer(private val templateEngine: TemplateEngine) : HtmlRenderer {

    init {
        templateEngine.setTemplateResolver(StringTemplateResolver())
    }

    override fun render(request: RenderRequest): String {
        val context = Context().apply {
            setVariable("translation", request.translations)
            setVariable("data", request.data)
        }

        return templateEngine.process(request.templateContent, context)
    }
}

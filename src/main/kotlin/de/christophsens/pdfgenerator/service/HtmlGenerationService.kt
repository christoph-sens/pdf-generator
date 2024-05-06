package de.christophsens.pdfgenerator.service

import de.christophsens.pdfgenerator.dto.TemplateDto
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.StringTemplateResolver

@Service
class HtmlGenerationService(private val templateEngine: TemplateEngine) {
    init {
        templateEngine.setTemplateResolver(StringTemplateResolver())
    }

    fun generateHtml(templateDto: TemplateDto, data: Any): String {
        // transform variables to map
        val translationMap: Map<String?, String?> =
            templateDto.translations?.associate { it.name to it.value } ?: emptyMap()

        // set context
        val context = Context().apply {
            setVariable("translation", translationMap)
            setVariable("data", data)
        }
        return templateEngine.process(templateDto.content, context)
    }
}
package de.christophsens.pdfgenerator.adapter.config

import de.christophsens.pdfgenerator.domain.service.PdfService
import de.christophsens.pdfgenerator.domain.service.TemplateService
import de.christophsens.pdfgenerator.domain.service.TranslationService
import de.christophsens.pdfgenerator.domain.port.inbound.GeneratePdfUseCase
import de.christophsens.pdfgenerator.domain.port.inbound.ManageTemplateUseCase
import de.christophsens.pdfgenerator.domain.port.inbound.ManageTranslationsUseCase
import de.christophsens.pdfgenerator.domain.port.outbound.HtmlRenderer
import de.christophsens.pdfgenerator.domain.port.outbound.PdfRenderer
import de.christophsens.pdfgenerator.domain.port.outbound.TemplateRepository
import de.christophsens.pdfgenerator.domain.port.outbound.TranslationRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DomainConfiguration {

    @Bean
    fun manageTemplateUseCase(templateRepository: TemplateRepository): ManageTemplateUseCase {
        return TemplateService(templateRepository)
    }

    @Bean
    fun manageTranslationsUseCase(
        translationRepository: TranslationRepository,
        templateRepository: TemplateRepository
    ): ManageTranslationsUseCase {
        return TranslationService(translationRepository, templateRepository)
    }

    @Bean
    fun generatePdfUseCase(
        templateRepository: TemplateRepository,
        htmlRenderer: HtmlRenderer,
        pdfRenderer: PdfRenderer
    ): GeneratePdfUseCase {
        return PdfService(templateRepository, htmlRenderer, pdfRenderer)
    }
}


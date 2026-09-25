package de.christophsens.pdfgenerator.config

import de.christophsens.pdfgenerator.application.service.PdfService
import de.christophsens.pdfgenerator.application.service.TemplateService
import de.christophsens.pdfgenerator.application.service.TranslationService
import de.christophsens.pdfgenerator.application.port.inbound.GeneratePdfUseCase
import de.christophsens.pdfgenerator.application.port.inbound.ManageTemplateUseCase
import de.christophsens.pdfgenerator.application.port.inbound.ManageTranslationsUseCase
import de.christophsens.pdfgenerator.application.port.outbound.HtmlRenderer
import de.christophsens.pdfgenerator.application.port.outbound.PdfRenderer
import de.christophsens.pdfgenerator.application.port.outbound.TemplateRepository
import de.christophsens.pdfgenerator.application.port.outbound.TranslationRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCaseConfiguration {

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


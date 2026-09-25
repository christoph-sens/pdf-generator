package de.christophsens.pdfgenerator.application.service

import de.christophsens.pdfgenerator.application.port.inbound.SaveTranslationsCommand
import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import de.christophsens.pdfgenerator.domain.model.Template
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TranslationServiceTest {

    private val templateRepository = InMemoryTemplateRepository()
    private val translationRepository = InMemoryTranslationRepository()
    private val service = TranslationService(translationRepository, templateRepository)

    @Test
    fun `saveTranslations stores translations for the given language`() {
        templateRepository.save(Template(key = INVOICE_DE, content = ""))

        service.saveTranslations(SaveTranslationsCommand(INVOICE_DE, DE, "title,Rechnung\nitem,Artikel"))

        val saved = translationRepository.saved.getValue(INVOICE_DE)
        assertEquals(mapOf("title" to "Rechnung", "item" to "Artikel"), saved.associate { it.name to it.value })
        assertEquals(setOf(DE), saved.map { it.languageCode }.toSet())
    }

    @Test
    fun `saveTranslations throws for unknown template`() {
        assertFailsWith<TemplateNotFoundException> {
            service.saveTranslations(SaveTranslationsCommand(INVOICE_DE, DE, "title,Rechnung"))
        }
    }
}

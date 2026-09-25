package de.christophsens.pdfgenerator.domain.service

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
        templateRepository.save(Template(name = "invoice", countryCode = "DE", content = ""))

        service.saveTranslations("invoice", "DE", "de", "title,Rechnung\nitem,Artikel")

        assertEquals(
            mapOf("title" to "Rechnung", "item" to "Artikel"),
            translationRepository.saved.associate { it.name to it.value }
        )
        assertEquals(setOf("de"), translationRepository.saved.map { it.languageCode }.toSet())
    }

    @Test
    fun `saveTranslations throws for unknown template`() {
        assertFailsWith<TemplateNotFoundException> {
            service.saveTranslations("missing", "DE", "de", "title,Rechnung")
        }
    }
}

package de.christophsens.pdfgenerator.domain.service

import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import de.christophsens.pdfgenerator.domain.model.Template
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TemplateServiceTest {

    private val templateRepository = InMemoryTemplateRepository()
    private val service = TemplateService(templateRepository)

    @Test
    fun `saveOrUpdate creates a new template`() {
        service.saveOrUpdate("invoice", "DE", "<html/>")

        assertEquals("<html/>", service.getTemplate("invoice", "DE").content)
    }

    @Test
    fun `saveOrUpdate keeps id when updating content`() {
        templateRepository.save(Template(id = 7, name = "invoice", countryCode = "DE", content = "old"))

        service.saveOrUpdate("invoice", "DE", "new")

        val template = service.getTemplate("invoice", "DE")
        assertEquals(7, template.id)
        assertEquals("new", template.content)
    }

    @Test
    fun `getTemplate throws for unknown template`() {
        assertFailsWith<TemplateNotFoundException> { service.getTemplate("missing", "DE") }
    }
}

package de.christophsens.pdfgenerator.application.service

import de.christophsens.pdfgenerator.application.port.inbound.SaveTemplateCommand
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
        service.saveOrUpdate(SaveTemplateCommand(INVOICE_DE, "<html/>"))

        assertEquals("<html/>", service.getTemplate(INVOICE_DE).content)
    }

    @Test
    fun `saveOrUpdate keeps id when updating content`() {
        templateRepository.save(Template(id = 7, key = INVOICE_DE, content = "old"))

        service.saveOrUpdate(SaveTemplateCommand(INVOICE_DE, "new"))

        val template = service.getTemplate(INVOICE_DE)
        assertEquals(7, template.id)
        assertEquals("new", template.content)
    }

    @Test
    fun `getTemplate throws for unknown template`() {
        assertFailsWith<TemplateNotFoundException> { service.getTemplate(INVOICE_DE) }
    }
}

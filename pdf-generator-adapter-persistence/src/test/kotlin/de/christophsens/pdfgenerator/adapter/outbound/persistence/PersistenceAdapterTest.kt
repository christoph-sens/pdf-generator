@file:Suppress("DEPRECATION")
package de.christophsens.pdfgenerator.adapter.outbound.persistence

import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import de.christophsens.pdfgenerator.domain.model.CountryCode
import de.christophsens.pdfgenerator.domain.model.LanguageCode
import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.model.TemplateKey
import de.christophsens.pdfgenerator.domain.model.Translation
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Import
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(TemplatePersistenceAdapter::class, TranslationPersistenceAdapter::class)
class PersistenceAdapterTest {

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val postgresql: PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:16.3"))
    }

    @Autowired
    lateinit var templateAdapter: TemplatePersistenceAdapter

    @Autowired
    lateinit var translationAdapter: TranslationPersistenceAdapter

    @Autowired
    lateinit var entityManager: EntityManager

    private val invoiceDe = TemplateKey("invoice", CountryCode("DE"))
    private val de = LanguageCode("de")
    private val en = LanguageCode("en")

    @Test
    fun `saves and finds templates by key`() {
        val saved = templateAdapter.save(Template(key = invoiceDe, content = "<html/>"))

        assertNotNull(saved.id)
        assertEquals("<html/>", templateAdapter.findByKey(invoiceDe)?.content)
        assertNull(templateAdapter.findByKey(TemplateKey("invoice", CountryCode("AT"))))
    }

    @Test
    fun `replaceAll replaces translations of one language only`() {
        templateAdapter.save(Template(key = invoiceDe, content = "<html/>"))
        translationAdapter.replaceAll(invoiceDe, de, listOf(translation("title", "Alt", de), translation("old", "Weg", de)))
        translationAdapter.replaceAll(invoiceDe, en, listOf(translation("title", "Invoice", en)))

        translationAdapter.replaceAll(invoiceDe, de, listOf(translation("title", "Rechnung", de)))
        entityManager.flush()
        entityManager.clear()

        val template = templateAdapter.findByKey(invoiceDe)!!
        assertEquals(mapOf("title" to "Rechnung"), template.translationsFor(de))
        assertEquals(mapOf("title" to "Invoice"), template.translationsFor(en))
    }

    @Test
    fun `replaceAll throws for unknown template`() {
        assertFailsWith<TemplateNotFoundException> {
            translationAdapter.replaceAll(invoiceDe, de, listOf(translation("title", "Rechnung", de)))
        }
    }

    private fun translation(name: String, value: String, languageCode: LanguageCode) =
        Translation(name = name, value = value, languageCode = languageCode)
}

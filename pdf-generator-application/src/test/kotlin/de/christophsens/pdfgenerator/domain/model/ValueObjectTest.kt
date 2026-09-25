package de.christophsens.pdfgenerator.domain.model

import de.christophsens.pdfgenerator.domain.exception.InvalidInputException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ValueObjectTest {

    @Test
    fun `accepts two letter codes`() {
        assertEquals("DE", CountryCode("DE").value)
        assertEquals("de", LanguageCode("de").value)
    }

    @Test
    fun `rejects codes that are not two letters`() {
        listOf("", "D", "DEU", "D1", " D").forEach { code ->
            assertFailsWith<InvalidInputException>(code) { CountryCode(code) }
            assertFailsWith<InvalidInputException>(code) { LanguageCode(code) }
        }
    }

    @Test
    fun `rejects blank template names`() {
        assertFailsWith<InvalidInputException> { TemplateKey(" ", CountryCode("DE")) }
    }
}

package de.christophsens.pdfgenerator.adapter.inbound.web

import de.christophsens.pdfgenerator.domain.exception.InvalidInputException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TranslationCsvParserTest {

    @Test
    fun `parses key value lines and ignores blank lines`() {
        assertEquals(
            mapOf("title" to "Rechnung", "item" to "Artikel"),
            TranslationCsvParser.parse("title, Rechnung\n\nitem,Artikel\n")
        )
    }

    @Test
    fun `treats the first line as data, not as header`() {
        assertEquals(mapOf("name" to "value"), TranslationCsvParser.parse("name,value"))
    }

    @Test
    fun `keeps commas in quoted and unquoted values`() {
        assertEquals(
            mapOf("a" to "x, y", "b" to "1,2"),
            TranslationCsvParser.parse("a,\"x, y\"\nb,1,2")
        )
    }

    @Test
    fun `key without value maps to empty string`() {
        assertEquals(mapOf("title" to ""), TranslationCsvParser.parse("title"))
    }

    @Test
    fun `rejects blank keys`() {
        assertFailsWith<InvalidInputException> { TranslationCsvParser.parse(",value") }
    }
}

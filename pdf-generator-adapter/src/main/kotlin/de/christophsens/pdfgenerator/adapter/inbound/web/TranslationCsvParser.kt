package de.christophsens.pdfgenerator.adapter.inbound.web

import de.christophsens.pdfgenerator.domain.exception.InvalidInputException
import org.apache.commons.csv.CSVFormat

/**
 * Parses an uploaded translation file with one `key,value` pair per line.
 *
 * There is no header line: every line is a translation. Values containing commas may be quoted;
 * unquoted extra columns are joined back into the value for compatibility with older uploads.
 */
object TranslationCsvParser {

    private val format = CSVFormat.DEFAULT.builder()
        .setTrim(true)
        .setIgnoreEmptyLines(true)
        .get()

    fun parse(csvContent: String): Map<String, String> {
        val records = format.parse(csvContent.reader()).use { parser -> parser.records.map { it.toList() } }
        return records.associate { fields ->
            val name = fields.first()
            if (name.isBlank()) throw InvalidInputException("Translation key must not be blank")
            name to fields.drop(1).joinToString(",")
        }
    }
}

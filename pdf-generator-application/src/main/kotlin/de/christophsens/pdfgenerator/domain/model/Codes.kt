package de.christophsens.pdfgenerator.domain.model

import de.christophsens.pdfgenerator.domain.exception.InvalidInputException

@JvmInline
value class CountryCode(val value: String) {
    init {
        if (!isTwoLetterCode(value)) throw InvalidInputException("Invalid country code: '$value'")
    }

    override fun toString() = value
}

@JvmInline
value class LanguageCode(val value: String) {
    init {
        if (!isTwoLetterCode(value)) throw InvalidInputException("Invalid language code: '$value'")
    }

    override fun toString() = value
}

private fun isTwoLetterCode(value: String) = value.length == 2 && value.all { it in 'a'..'z' || it in 'A'..'Z' }

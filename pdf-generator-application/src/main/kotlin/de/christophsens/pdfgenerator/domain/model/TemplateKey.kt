package de.christophsens.pdfgenerator.domain.model

import de.christophsens.pdfgenerator.domain.exception.InvalidInputException

/** Natural key of a template: one template per name and country. */
data class TemplateKey(val name: String, val countryCode: CountryCode) {
    init {
        if (name.isBlank()) throw InvalidInputException("Template name must not be blank")
    }
}

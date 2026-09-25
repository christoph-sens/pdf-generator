package de.christophsens.pdfgenerator.domain.exception

import de.christophsens.pdfgenerator.domain.model.TemplateKey

sealed class DomainException(message: String) : RuntimeException(message)

class TemplateNotFoundException(key: TemplateKey) :
    DomainException("Template with name=${key.name} and countryCode=${key.countryCode} not found")

class InvalidInputException(message: String) : DomainException(message)

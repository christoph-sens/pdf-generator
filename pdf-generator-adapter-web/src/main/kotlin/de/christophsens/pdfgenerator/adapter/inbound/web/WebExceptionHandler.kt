package de.christophsens.pdfgenerator.adapter.inbound.web

import de.christophsens.pdfgenerator.domain.exception.InvalidInputException
import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import tools.jackson.core.JacksonException

@RestControllerAdvice
class WebExceptionHandler {

    @ExceptionHandler(TemplateNotFoundException::class)
    fun handleTemplateNotFound(ex: TemplateNotFoundException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)

    @ExceptionHandler(InvalidInputException::class)
    fun handleInvalidInput(ex: InvalidInputException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)

    @ExceptionHandler(JacksonException::class)
    fun handleInvalidJson(ex: JacksonException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body must be a JSON object")
}

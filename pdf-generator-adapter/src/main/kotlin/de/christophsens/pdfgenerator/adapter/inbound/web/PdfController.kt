package de.christophsens.pdfgenerator.adapter.inbound.web

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import de.christophsens.pdfgenerator.domain.port.inbound.GeneratePdfUseCase
import de.christophsens.pdfgenerator.domain.port.inbound.ManageTemplateUseCase
import de.christophsens.pdfgenerator.domain.port.inbound.ManageTranslationsUseCase
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class PdfController(
    private val manageTemplateUseCase: ManageTemplateUseCase,
    private val manageTranslationsUseCase: ManageTranslationsUseCase,
    private val generatePdfUseCase: GeneratePdfUseCase,
    private val objectMapper: ObjectMapper
) {

    @PutMapping("/template/{name}/{countryCode}")
    fun addTemplate(
        @PathVariable name: String,
        @PathVariable countryCode: String,
        @RequestBody template: String
    ): ResponseEntity<Void> {
        manageTemplateUseCase.saveOrUpdate(name, countryCode, template)
        return ResponseEntity.ok().build()
    }

    @PutMapping(
        "/translations/{templateName}/{countryCode}/{languageCode}",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun addTranslations(
        @PathVariable templateName: String,
        @PathVariable countryCode: String,
        @PathVariable languageCode: String,
        @RequestParam file: MultipartFile
    ): ResponseEntity<Void> {
        if (file.isEmpty || countryCode.isBlank() || languageCode.isBlank() || templateName.isBlank()) {
            return ResponseEntity.badRequest().build()
        }

        val csvContent = file.inputStream.bufferedReader().use { it.readText() }
        manageTranslationsUseCase.saveTranslations(templateName, countryCode, languageCode, csvContent)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/pdf/{name}/{countryCode}/{languageCode}", produces = [MediaType.APPLICATION_PDF_VALUE])
    fun getPdfDocument(
        @PathVariable name: String,
        @PathVariable countryCode: String,
        @PathVariable languageCode: String,
        @RequestBody jsonString: String
    ): ResponseEntity<ByteArray> {
        val jsonNode = objectMapper.readTree(jsonString)
        val map: Map<String, Any> = objectMapper.convertValue(jsonNode, object : TypeReference<Map<String, Any>>() {})
        val pdfBytes = generatePdfUseCase.generate(name, countryCode, languageCode, map)

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PDF
        headers.setContentDispositionFormData("filename", "template.pdf")
        headers.contentLength = pdfBytes.size.toLong()

        return ResponseEntity.ok().headers(headers).body(pdfBytes)
    }

    @ExceptionHandler(TemplateNotFoundException::class)
    fun handleTemplateNotFound(ex: TemplateNotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }
}


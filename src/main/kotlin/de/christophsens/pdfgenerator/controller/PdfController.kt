package de.christophsens.pdfgenerator.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import de.christophsens.pdfgenerator.entity.TemplateEntity
import de.christophsens.pdfgenerator.service.PdfService
import de.christophsens.pdfgenerator.service.TemplateService
import de.christophsens.pdfgenerator.service.TranslationService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class PdfController(
    private val templateService: TemplateService, private val translationService: TranslationService,
    private val pdfService: PdfService
) {

    @PutMapping("/template/{name}/{countryCode}")
    fun addTemplate(
        @PathVariable name: String,
        @PathVariable countryCode: String,
        @RequestBody template: String
    ): HttpStatus {
        val templateEntity = TemplateEntity()
        templateEntity.name = name
        templateEntity.countryCode = countryCode
        templateEntity.content = template
        templateService.saveOrUpdate(templateEntity)
        return HttpStatus.OK
    }

    @PutMapping(
        "/translations/{templateName}/{countryCode}/{languageCode}",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun addTranslations(
        @PathVariable templateName: String,
        @PathVariable countryCode: String,
        @PathVariable languageCode: String,
        @RequestBody file: MultipartFile
    ): HttpStatus {
        if (file.isEmpty || countryCode.isBlank() || languageCode.isBlank() || templateName.isBlank()) {
            return HttpStatus.BAD_REQUEST
        }
        translationService.saveTranslations(templateName, countryCode, languageCode, file)
        return HttpStatus.OK
    }

    @PostMapping("/pdf/{name}/{countryCode}/{languageCode}", produces = [MediaType.APPLICATION_PDF_VALUE])
    fun getPdfDocument(
        @PathVariable name: String,
        @PathVariable countryCode: String,
        @PathVariable languageCode: String,
        @RequestBody jsonString:String
    ): ResponseEntity<ByteArray> {
        val objectMapper = ObjectMapper()
        val jsonNode = objectMapper.readTree(jsonString)
        val map: Map<String, Any> = objectMapper.convertValue(jsonNode, object : TypeReference<Map<String, Any>>() {})
        val pdfBytes = pdfService.getPdf(name, countryCode, languageCode, map)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PDF
        headers.setContentDispositionFormData("filename", "template.pdf")
        headers.contentLength = pdfBytes.size.toLong()
        return ResponseEntity(pdfBytes, headers, HttpStatus.OK)

    }


}
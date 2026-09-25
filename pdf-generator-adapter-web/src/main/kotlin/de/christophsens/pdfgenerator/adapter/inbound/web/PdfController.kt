package de.christophsens.pdfgenerator.adapter.inbound.web

import de.christophsens.pdfgenerator.application.port.inbound.GeneratePdfCommand
import de.christophsens.pdfgenerator.application.port.inbound.GeneratePdfUseCase
import de.christophsens.pdfgenerator.application.port.inbound.ManageTemplateUseCase
import de.christophsens.pdfgenerator.application.port.inbound.ManageTranslationsUseCase
import de.christophsens.pdfgenerator.application.port.inbound.SaveTemplateCommand
import de.christophsens.pdfgenerator.application.port.inbound.SaveTranslationsCommand
import de.christophsens.pdfgenerator.domain.model.CountryCode
import de.christophsens.pdfgenerator.domain.model.LanguageCode
import de.christophsens.pdfgenerator.domain.model.TemplateKey
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.json.JsonMapper

@RestController
class PdfController(
    private val manageTemplateUseCase: ManageTemplateUseCase,
    private val manageTranslationsUseCase: ManageTranslationsUseCase,
    private val generatePdfUseCase: GeneratePdfUseCase,
    private val jsonMapper: JsonMapper
) {

    @PutMapping("/template/{name}/{countryCode}")
    fun addTemplate(
        @PathVariable name: String,
        @PathVariable countryCode: String,
        @RequestBody template: String
    ): ResponseEntity<Void> {
        manageTemplateUseCase.saveOrUpdate(SaveTemplateCommand(TemplateKey(name, CountryCode(countryCode)), template))
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
        if (file.isEmpty) {
            return ResponseEntity.badRequest().build()
        }

        val csvContent = file.inputStream.bufferedReader().use { it.readText() }
        manageTranslationsUseCase.saveTranslations(
            SaveTranslationsCommand(
                TemplateKey(templateName, CountryCode(countryCode)),
                LanguageCode(languageCode),
                TranslationCsvParser.parse(csvContent)
            )
        )
        return ResponseEntity.ok().build()
    }

    // The body is taken as String and parsed here so that clients need not send a JSON content type.
    @PostMapping("/pdf/{name}/{countryCode}/{languageCode}", produces = [MediaType.APPLICATION_PDF_VALUE])
    fun getPdfDocument(
        @PathVariable name: String,
        @PathVariable countryCode: String,
        @PathVariable languageCode: String,
        @RequestBody jsonString: String
    ): ResponseEntity<ByteArray> {
        val data = jsonMapper.readValue(jsonString, object : TypeReference<Map<String, Any?>>() {})
        val pdfBytes = generatePdfUseCase.generate(
            GeneratePdfCommand(TemplateKey(name, CountryCode(countryCode)), LanguageCode(languageCode), data)
        )

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PDF
        headers.setContentDispositionFormData("filename", "template.pdf")
        headers.contentLength = pdfBytes.size.toLong()

        return ResponseEntity.ok().headers(headers).body(pdfBytes)
    }
}

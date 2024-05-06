package de.christophsens.pdfgenerator.util

import de.christophsens.pdfgenerator.dto.TemplateDto
import de.christophsens.pdfgenerator.dto.TranslationDto
import de.christophsens.pdfgenerator.entity.TemplateEntity
import de.christophsens.pdfgenerator.entity.TranslationEntity
import org.springframework.web.multipart.MultipartFile
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream

fun convertMultiPartFileToMap(file: MultipartFile): Map<String, String> {
    return file.inputStream.bufferedReader().useLines { line ->
        line.map { line1 ->
            val splitLine = line1.split(",").map { it.trim() }
            splitLine[0] to splitLine[1]
        }.toMap()
    }
}

fun mapTemplateEntityToDTO(entity: TemplateEntity): TemplateDto {
    return TemplateDto(
        id = entity.id,
        name = entity.name,
        countryCode = entity.countryCode,
        content = entity.content,
        translations = entity.translationEntities?.map { mapTranslationEntityToDTO(it) }
    )
}

fun mapTranslationEntityToDTO(entity: TranslationEntity): TranslationDto {
    return TranslationDto(
        id = entity.id,
        name = entity.name,
        value = entity.value,
        languageCode = entity.languageCode
    )
}

fun generatePdfFromHtml(htmlContent: String): ByteArray {
    val renderer = ITextRenderer()
    renderer.setDocumentFromString(htmlContent)
    renderer.layout()
    val outputStream = ByteArrayOutputStream()
    renderer.createPDF(outputStream)
    return outputStream.toByteArray()
}
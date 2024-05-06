package de.christophsens.pdfgenerator.service

import de.christophsens.pdfgenerator.entity.TranslationEntity
import de.christophsens.pdfgenerator.repository.TemplateRepository
import de.christophsens.pdfgenerator.repository.TranslationRepository
import de.christophsens.pdfgenerator.util.convertMultiPartFileToMap
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class TranslationService(private val translationRepository: TranslationRepository, private val templateRepository: TemplateRepository) {

    fun saveTranslations(templateName: String, countryCode: String, languageCode: String, file: MultipartFile) {
        val templateEntity = templateRepository.findByNameAndCountryCode(templateName, countryCode).first();

        val translationMap = convertMultiPartFileToMap(file)
        val list = mutableListOf<TranslationEntity>()
        translationMap.forEach { translation ->
            val translationEntity = TranslationEntity()
            translationEntity.name = translation.key
            translationEntity.value = translation.value
            translationEntity.languageCode = languageCode
            translationEntity.templateEntity = templateEntity
            list.add(translationEntity)
        }
        translationRepository.saveAll(list)

    }
}
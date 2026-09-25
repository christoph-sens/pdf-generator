package de.christophsens.pdfgenerator.adapter.outbound.persistence

import de.christophsens.pdfgenerator.adapter.outbound.persistence.entity.toJpaEntity
import de.christophsens.pdfgenerator.adapter.outbound.persistence.repository.SpringDataTemplateRepository
import de.christophsens.pdfgenerator.adapter.outbound.persistence.repository.SpringDataTranslationRepository
import de.christophsens.pdfgenerator.application.port.outbound.TranslationRepository
import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import de.christophsens.pdfgenerator.domain.model.LanguageCode
import de.christophsens.pdfgenerator.domain.model.TemplateKey
import de.christophsens.pdfgenerator.domain.model.Translation
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TranslationPersistenceAdapter(
    private val springDataTranslationRepository: SpringDataTranslationRepository,
    private val springDataTemplateRepository: SpringDataTemplateRepository
) : TranslationRepository {

    @Transactional
    override fun replaceAll(templateKey: TemplateKey, languageCode: LanguageCode, translations: List<Translation>) {
        val templateEntity = springDataTemplateRepository
            .findByNameAndCountryCode(templateKey.name, templateKey.countryCode.value)
            .firstOrNull() ?: throw TemplateNotFoundException(templateKey)

        springDataTranslationRepository.deleteByTemplateAndLanguage(
            templateKey.name, templateKey.countryCode.value, languageCode.value
        )

        val jpaEntities = translations.map { translation ->
            translation.toJpaEntity().also { it.templateEntity = templateEntity }
        }
        springDataTranslationRepository.saveAll(jpaEntities)
    }
}

package de.christophsens.pdfgenerator.adapter.outbound.persistence

import de.christophsens.pdfgenerator.adapter.outbound.persistence.entity.toJpaEntity
import de.christophsens.pdfgenerator.adapter.outbound.persistence.repository.SpringDataTemplateRepository
import de.christophsens.pdfgenerator.adapter.outbound.persistence.repository.SpringDataTranslationRepository
import de.christophsens.pdfgenerator.application.port.outbound.TranslationRepository
import de.christophsens.pdfgenerator.domain.exception.TemplateNotFoundException
import de.christophsens.pdfgenerator.domain.model.TemplateKey
import de.christophsens.pdfgenerator.domain.model.Translation
import org.springframework.stereotype.Component

@Component
class TranslationPersistenceAdapter(
    private val springDataTranslationRepository: SpringDataTranslationRepository,
    private val springDataTemplateRepository: SpringDataTemplateRepository
) : TranslationRepository {

    override fun saveAll(templateKey: TemplateKey, translations: List<Translation>) {
        val templateEntity = springDataTemplateRepository
            .findByNameAndCountryCode(templateKey.name, templateKey.countryCode.value)
            .firstOrNull() ?: throw TemplateNotFoundException(templateKey)

        val jpaEntities = translations.map { translation ->
            translation.toJpaEntity().also { it.templateEntity = templateEntity }
        }
        springDataTranslationRepository.saveAll(jpaEntities)
    }

    override fun deleteAll() {
        springDataTranslationRepository.deleteAll()
    }
}

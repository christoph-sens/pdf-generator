package de.christophsens.pdfgenerator.adapter.outbound.persistence

import de.christophsens.pdfgenerator.adapter.outbound.persistence.entity.toJpaEntity
import de.christophsens.pdfgenerator.adapter.outbound.persistence.repository.SpringDataTemplateRepository
import de.christophsens.pdfgenerator.adapter.outbound.persistence.repository.SpringDataTranslationRepository
import de.christophsens.pdfgenerator.domain.model.Translation
import de.christophsens.pdfgenerator.domain.port.outbound.TranslationRepository
import org.springframework.stereotype.Component

@Component
class TranslationPersistenceAdapter(
    private val springDataTranslationRepository: SpringDataTranslationRepository,
    private val springDataTemplateRepository: SpringDataTemplateRepository
) : TranslationRepository {

    override fun saveAll(translations: List<Translation>, templateName: String, countryCode: String) {
        // Find the template to establish the relationship
        val templateEntities = springDataTemplateRepository.findByNameAndCountryCode(templateName, countryCode)
        if (templateEntities.isEmpty()) {
            throw IllegalArgumentException("Template not found")
        }
        val templateEntity = templateEntities.first()

        val jpaEntities = translations.map { translation ->
            val jpaEntity = translation.toJpaEntity()
            jpaEntity.templateEntity = templateEntity
            jpaEntity
        }
        springDataTranslationRepository.saveAll(jpaEntities)
    }

    override fun deleteAll() {
        springDataTranslationRepository.deleteAll()
    }
}






package de.christophsens.pdfgenerator.adapter.outbound.persistence

import de.christophsens.pdfgenerator.adapter.outbound.persistence.entity.toModel
import de.christophsens.pdfgenerator.adapter.outbound.persistence.entity.toJpaEntity
import de.christophsens.pdfgenerator.adapter.outbound.persistence.repository.SpringDataTemplateRepository
import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.port.outbound.TemplateRepository
import org.springframework.stereotype.Component

@Component
class TemplatePersistenceAdapter(
    private val springDataTemplateRepository: SpringDataTemplateRepository
) : TemplateRepository {

    override fun findByNameAndCountryCode(name: String, countryCode: String): Template? {
        val entities = springDataTemplateRepository.findByNameAndCountryCode(name, countryCode)
        return if (entities.isNotEmpty()) entities.first().toModel() else null
    }

    override fun save(template: Template): Template {
        val jpaEntity = template.toJpaEntity()
        val saved = springDataTemplateRepository.save(jpaEntity)
        return saved.toModel()
    }
}


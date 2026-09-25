package de.christophsens.pdfgenerator.adapter.outbound.persistence

import de.christophsens.pdfgenerator.adapter.outbound.persistence.entity.toJpaEntity
import de.christophsens.pdfgenerator.adapter.outbound.persistence.entity.toModel
import de.christophsens.pdfgenerator.adapter.outbound.persistence.repository.SpringDataTemplateRepository
import de.christophsens.pdfgenerator.application.port.outbound.TemplateRepository
import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.model.TemplateKey
import org.springframework.stereotype.Component

@Component
class TemplatePersistenceAdapter(
    private val springDataTemplateRepository: SpringDataTemplateRepository
) : TemplateRepository {

    override fun findByKey(key: TemplateKey): Template? =
        springDataTemplateRepository.findByNameAndCountryCode(key.name, key.countryCode.value).firstOrNull()?.toModel()

    override fun save(template: Template): Template {
        val jpaEntity = template.toJpaEntity()
        val saved = springDataTemplateRepository.save(jpaEntity)
        return saved.toModel()
    }
}

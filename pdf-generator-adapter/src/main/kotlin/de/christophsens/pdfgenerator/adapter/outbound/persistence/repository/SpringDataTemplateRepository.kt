package de.christophsens.pdfgenerator.adapter.outbound.persistence.repository

import de.christophsens.pdfgenerator.adapter.outbound.persistence.entity.TemplateJpaEntity
import org.springframework.data.repository.CrudRepository

interface SpringDataTemplateRepository : CrudRepository<TemplateJpaEntity, Long> {
    fun findByNameAndCountryCode(name: String, countryCode: String): List<TemplateJpaEntity>
}


package de.christophsens.pdfgenerator.adapter.outbound.persistence.repository

import de.christophsens.pdfgenerator.adapter.outbound.persistence.entity.TranslationJpaEntity
import org.springframework.data.repository.CrudRepository

interface SpringDataTranslationRepository : CrudRepository<TranslationJpaEntity, Long>


package de.christophsens.pdfgenerator.repository

import de.christophsens.pdfgenerator.entity.TranslationEntity
import org.springframework.data.repository.CrudRepository

interface TranslationRepository : CrudRepository<TranslationEntity, Long> 
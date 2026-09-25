package de.christophsens.pdfgenerator.adapter.outbound.persistence.repository

import de.christophsens.pdfgenerator.adapter.outbound.persistence.entity.TranslationJpaEntity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface SpringDataTranslationRepository : CrudRepository<TranslationJpaEntity, Long> {

    // Bulk delete runs immediately; a derived delete would be flushed after the new inserts
    // and violate the unique index on (template_name, country_code, language_code, name).
    @Modifying(flushAutomatically = true)
    @Query(
        "delete from translation where template_name = :templateName and country_code = :countryCode and language_code = :languageCode",
        nativeQuery = true
    )
    fun deleteByTemplateAndLanguage(templateName: String, countryCode: String, languageCode: String)
}

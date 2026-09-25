package de.christophsens.pdfgenerator.adapter.outbound.persistence.entity

import de.christophsens.pdfgenerator.domain.model.CountryCode
import de.christophsens.pdfgenerator.domain.model.LanguageCode
import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.model.TemplateKey
import de.christophsens.pdfgenerator.domain.model.Translation

fun TemplateJpaEntity.toModel(): Template {
    return Template(
        id = this.id,
        key = TemplateKey(this.name!!, CountryCode(this.countryCode!!)),
        content = this.content ?: "",
        translations = this.translationEntities?.map { it.toModel() } ?: emptyList()
    )
}

fun Template.toJpaEntity(): TemplateJpaEntity {
    val entity = TemplateJpaEntity()
    entity.id = this.id
    entity.name = this.key.name
    entity.countryCode = this.key.countryCode.value
    entity.content = this.content
    entity.translationEntities = this.translations.map { it.toJpaEntity() }.toMutableSet()
    return entity
}

fun TranslationJpaEntity.toModel(): Translation {
    return Translation(
        id = this.id,
        name = this.name!!,
        value = this.value ?: "",
        languageCode = LanguageCode(this.languageCode!!)
    )
}

fun Translation.toJpaEntity(): TranslationJpaEntity {
    val entity = TranslationJpaEntity()
    entity.id = this.id
    entity.name = this.name
    entity.value = this.value
    entity.languageCode = this.languageCode.value
    return entity
}

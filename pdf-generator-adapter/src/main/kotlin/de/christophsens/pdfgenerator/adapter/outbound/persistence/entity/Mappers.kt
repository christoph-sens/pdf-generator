package de.christophsens.pdfgenerator.adapter.outbound.persistence.entity

import de.christophsens.pdfgenerator.domain.model.Template
import de.christophsens.pdfgenerator.domain.model.Translation

fun TemplateJpaEntity.toModel(): Template {
    return Template(
        id = this.id,
        name = this.name ?: "",
        countryCode = this.countryCode ?: "",
        content = this.content ?: "",
        translations = this.translationEntities?.map { it.toModel() } ?: emptyList()
    )
}

fun Template.toJpaEntity(): TemplateJpaEntity {
    val entity = TemplateJpaEntity()
    entity.id = this.id
    entity.name = this.name
    entity.countryCode = this.countryCode
    entity.content = this.content
    entity.translationEntities = this.translations.map { it.toJpaEntity() }.toMutableSet()
    return entity
}

fun TranslationJpaEntity.toModel(): Translation {
    return Translation(
        id = this.id,
        name = this.name ?: "",
        value = this.value ?: "",
        languageCode = this.languageCode ?: ""
    )
}

fun Translation.toJpaEntity(): TranslationJpaEntity {
    val entity = TranslationJpaEntity()
    entity.id = this.id
    entity.name = this.name
    entity.value = this.value
    entity.languageCode = this.languageCode
    return entity
}


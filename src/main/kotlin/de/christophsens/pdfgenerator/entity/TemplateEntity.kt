package de.christophsens.pdfgenerator.entity

import jakarta.persistence.*

@Entity
@Table(name = "template")
open class TemplateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "template_id_gen")
    @SequenceGenerator(name = "template_id_gen", sequenceName = "template_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "country_code", nullable = false, length = 2)
    open var countryCode: String? = null

    @Column(name = "content", length = Integer.MAX_VALUE)
    open var content: String? = null

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "templateEntity")
    open var translationEntities: MutableSet<TranslationEntity>? = mutableSetOf()
}
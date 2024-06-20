package de.christophsens.pdfgenerator.entity

import jakarta.persistence.*

@Entity
@Table(name = "translation")
open class TranslationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "translation_id_gen")
    @SequenceGenerator(name = "translation_id_gen", sequenceName = "translation_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "value", length = Integer.MAX_VALUE)
    open var value: String? = null

    @Column(name = "language_code", nullable = false, length = 2)
    open var languageCode: String? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns(
        JoinColumn(name = "country_code", referencedColumnName = "country_code", insertable = true),
        JoinColumn(name = "template_name", referencedColumnName = "name", insertable = true)
    )
    open var templateEntity: TemplateEntity? = null
}
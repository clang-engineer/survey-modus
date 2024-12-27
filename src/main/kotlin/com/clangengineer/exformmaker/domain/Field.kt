package com.clangengineer.surveymodus.domain

import com.clangengineer.surveymodus.domain.embeddable.FieldAttribute
import com.clangengineer.surveymodus.domain.embeddable.FieldDisplay
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "tbl_field")
@SecondaryTable(
    name = "tbl_field_attribute",
    pkJoinColumns = [PrimaryKeyJoinColumn(name = "field_id", referencedColumnName = "id")]
)
@SecondaryTable(
    name = "tbl_field_display",
    pkJoinColumns = [PrimaryKeyJoinColumn(name = "field_id", referencedColumnName = "id")]
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Field(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,

    @get: NotNull
    @get: Size(min = 5, max = 100)
    @Column(name = "title", length = 100, nullable = false)
    var title: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "activated")
    var activated: Boolean? = null,

    @Embedded
    @AttributeOverride(name = "type", column = Column(name = "type", table = "tbl_field_attribute"))
    @AttributeOverride(
        name = "defaultValue",
        column = Column(name = "default_value", table = "tbl_field_attribute")
    )
    var attribute: FieldAttribute? = null,

    @Embedded
    @AttributeOverride(
        name = "orderNo",
        column = Column(name = "order_no", table = "tbl_field_display")
    )
    var display: FieldDisplay? = null,

    @ElementCollection
    @CollectionTable(name = "tbl_field_lookup", joinColumns = [JoinColumn(name = "field_id")])
    @Column(name = "title", nullable = false)
    @OrderColumn(name = "order_no")
    var lookups: List<String> = mutableListOf()

) : Serializable {
    @ManyToOne(optional = false)
    @NotNull
    var form: Form? = null
    fun form(form: Form?): Field {
        this.form = form
        return this
    }

    // todo: lookup에 null이 포함되어 오는데, 다른 해결 방법 찾아보기
    @PostLoad
    fun postLoad() {
        lookups = lookups.filterNotNull()
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Field) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Field{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", activated='" + activated + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

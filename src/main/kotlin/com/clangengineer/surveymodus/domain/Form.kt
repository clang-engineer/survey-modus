package com.clangengineer.surveymodus.domain

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "tbl_form")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Form(
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

    @Column(name = "order_no")
    var orderNo: Int? = null
) : Serializable {
    @ManyToOne(optional = false)
    @NotNull
    var user: User? = null
    fun user(user: User?): Form {
        this.user = user
        return this
    }

    @ManyToOne(optional = false)
    @NotNull
    var category: Category? = null
    fun category(category: Category?): Form {
        this.category = category
        return this
    }
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Form) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Form{" +
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

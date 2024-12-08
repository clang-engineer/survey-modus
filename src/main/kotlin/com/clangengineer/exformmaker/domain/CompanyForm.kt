package com.clangengineer.exformmaker.domain

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "tbl_company_form")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CompanyForm(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,
) : Serializable {
    @ManyToOne(optional = false)
    @NotNull
    var company: Company? = null

    @ManyToOne(optional = false)
    @NotNull
    var form: Form? = null

    fun company(company: Company?): CompanyForm {
        this.company = company
        return this
    }

    fun form(form: Form?): CompanyForm {
        this.form = form
        return this
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CompanyForm) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "CompanyForm{" +
            "id=" + id +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

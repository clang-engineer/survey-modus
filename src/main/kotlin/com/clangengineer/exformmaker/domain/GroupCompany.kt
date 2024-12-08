package com.clangengineer.exformmaker.domain

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "tbl_group_company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
data class GroupCompany(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,
) : Serializable {

    @ManyToOne(optional = false)
    @NotNull
    var group: Group? = null

    @ManyToOne(optional = false)
    @NotNull
    var company: Company? = null

    fun group(group: Group?): GroupCompany {
        this.group = group
        return this
    }
    fun company(company: Company?): GroupCompany {
        this.company = company
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GroupCompany) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "GroupCompany{" +
            "id=" + id +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

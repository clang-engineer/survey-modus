package com.clangengineer.exformmaker.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "tbl_user_company ")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
data class UserCompany(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,
) : Serializable {

    @ManyToOne(optional = false)
    @NotNull
    var user: User? = null

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = [
            "user",
        ],
        allowSetters = true
    )
    var company: Company? = null

    fun user(user: User?): UserCompany {
        this.user = user
        return this
    }
    fun company(company: Company?): UserCompany {
        this.company = company
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserCompany) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "UserCompany{" +
            "id=" + id +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

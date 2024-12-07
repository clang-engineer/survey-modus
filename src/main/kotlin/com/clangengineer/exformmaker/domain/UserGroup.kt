package com.clangengineer.exformmaker.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "tbl_user_group ")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
data class UserGroup(
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
    var group: Group? = null

    fun user(user: User?): UserGroup {
        this.user = user
        return this
    }
    fun group(group: Group?): UserGroup {
        this.group = group
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserGroup) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "UserGroup{" +
            "id=" + id +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

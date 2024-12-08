package com.clangengineer.exformmaker.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "tbl_group_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
data class GroupUser(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,
) : Serializable {
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = [ "user", ],
        allowSetters = true
    )
    var group: Group? = null

    @ManyToOne(optional = false)
    @NotNull
    var user: User? = null

    fun group(group: Group?): GroupUser {
        this.group = group
        return this
    }

    fun user(user: User?): GroupUser {
        this.user = user
        return this
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GroupUser) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "GroupUser{" +
            "id=" + id +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

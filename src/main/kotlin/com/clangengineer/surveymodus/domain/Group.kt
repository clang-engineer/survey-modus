package com.clangengineer.surveymodus.domain

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "tbl_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Group(
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

) : Serializable {
    @ManyToOne(optional = false)
    @NotNull
    var user: User? = null
    fun user(user: User?): Group {
        this.user = user
        return this
    }

    @ManyToMany
    @JoinTable(
        name = "tbl_group_user",
        joinColumns = [JoinColumn(name = "group_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")]
    )
    var users: MutableSet<User> = mutableSetOf()
    fun users(users: MutableSet<User>): Group {
        this.users = users
        return this
    }

    @ManyToMany
    @JoinTable(
        name = "tbl_group_company",
        joinColumns = [JoinColumn(name = "group_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "company_id", referencedColumnName = "id")]
    )
    var companies: MutableSet<Company> = mutableSetOf()
    fun companies(companies: MutableSet<Company>): Group {
        this.companies = companies
        return this
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Group) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Group{" +
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

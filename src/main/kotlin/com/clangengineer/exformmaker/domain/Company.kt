package com.clangengineer.surveymodus.domain

import com.clangengineer.surveymodus.domain.embeddable.Staff
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "tbl_company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Company(
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
    fun user(user: User?): Company {
        this.user = user
        return this
    }

    @ManyToMany
    @JoinTable(
        name = "tbl_company_form",
        joinColumns = [JoinColumn(name = "company_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "form_id", referencedColumnName = "id")]
    )
    var forms: MutableSet<Form> = mutableSetOf()
    fun forms(forms: MutableSet<Form>): Company {
        this.forms = forms
        return this
    }

    @ElementCollection
    @CollectionTable(name = "tbl_staff", joinColumns = [JoinColumn(name = "company_id")])
    var staffs: MutableSet<Staff> = mutableSetOf()
    fun staffs(staffs: MutableSet<Staff>): Company {
        this.staffs = staffs
        return this
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Company) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Company{" +
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

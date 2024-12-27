package com.clangengineer.surveymodus.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.*

/**
 * A UserPoint.
 */

@Entity
@Table(name = "user_point")

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
data class UserPoint(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
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
    var point: Point? = null

    fun user(user: User?): UserPoint {
        this.user = user
        return this
    }
    fun point(point: Point?): UserPoint {
        this.point = point
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserPoint) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "UserPoint{" +
            "id=" + id +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

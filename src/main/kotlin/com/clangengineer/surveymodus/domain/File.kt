package com.clangengineer.surveymodus.domain

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.io.Serializable
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "tbl_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
data class File(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,

    @get: NotNull
    @get: Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    var name: String? = null,

    @Column(name = "path")
    var path: String? = null,

    @Column(name = "size")
    var size: Long? = null,

    @Column(name = "type")
    var type: String? = null,

    @Column(name = "created_by")
    var createdBy: String? = null,

    @Column(name = "created_date")
    var createdDate: Instant? = null,

    @Column(name = "last_modified_by")
    var lastModifiedBy: String? = null,

    @Column(name = "last_modified_date")
    var lastModifiedDate: Instant? = null,
) : Serializable {
    val hashKey: String
        get() {
            return getSHA512(id.toString())
        }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is File) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "File{" +
            "id=$id" +
            ", name='$name'" +
            ", path='$path'" +
            ", size=$size" +
            ", type='$type'" +
            "}"
    }

    fun getSHA512(input: String): String {
        val digest = MessageDigest.getInstance("SHA-512")
        val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))

        return hash.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

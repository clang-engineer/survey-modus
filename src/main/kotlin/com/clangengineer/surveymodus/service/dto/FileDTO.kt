package com.clangengineer.surveymodus.service.dto

import com.clangengineer.surveymodus.service.getSHA512
import java.io.Serializable
import java.time.Instant
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@SuppressWarnings("common-java:DuplicatedBlocks")
data class FileDTO(
    var id: Long? = null,

    @get: NotNull
    @get: Size(max = 100)
    var filename: String? = null,

    var filepath: String? = null,

    var createdBy: String? = null,

    var createdDate: Instant? = null,

    var lastModifiedBy: String? = null,

    var lastModifiedDate: Instant? = null,
) : Serializable {
    val hashKey: String
        get() {
            return getSHA512(id.toString())
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileDTO) return false
        val fileDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, fileDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}

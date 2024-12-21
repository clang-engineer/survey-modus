package com.clangengineer.exformmaker.service.dto

import com.clangengineer.exformmaker.domain.enumeration.level
import java.io.Serializable
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A DTO for the [com.clangengineer.exformmaker.domain.Point] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class PointDTO(

    var id: Long? = null,

    @get: NotNull
    @get: Size(min = 5, max = 100)
    var title: String? = null,

    var description: String? = null,

    var activated: Boolean? = null,

    var type: level? = null,

    var user: UserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PointDTO) return false
        val pointDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, pointDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}

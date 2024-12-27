package com.clangengineer.surveymodus.service.dto

import java.io.Serializable
import java.util.Objects
import javax.validation.constraints.*

/**
 * A DTO for the [com.clangengineer.surveymodus.domain.UserPoint] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class UserPointDTO(

    var id: Long? = null,

    var user: UserDTO? = null,

    var point: PointDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserPointDTO) return false
        val userPointDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, userPointDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}

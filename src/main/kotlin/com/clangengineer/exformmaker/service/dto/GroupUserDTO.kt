package com.clangengineer.exformmaker.service.dto

import java.io.Serializable
import java.util.*

@SuppressWarnings("common-java:DuplicatedBlocks")
data class GroupUserDTO(
    var id: Long? = null,
    var group: GroupDTO? = null,
    var user: UserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GroupUserDTO) return false
        val groupUserDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, groupUserDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}

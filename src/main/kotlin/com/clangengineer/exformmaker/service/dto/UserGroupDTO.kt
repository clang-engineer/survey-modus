package com.clangengineer.exformmaker.service.dto

import java.io.Serializable
import java.util.Objects
import javax.validation.constraints.*

@SuppressWarnings("common-java:DuplicatedBlocks")
data class UserGroupDTO(

    var id: Long? = null,

    var user: UserDTO? = null,

    var group: GroupDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserGroupDTO) return false
        val userGroupDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, userGroupDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}

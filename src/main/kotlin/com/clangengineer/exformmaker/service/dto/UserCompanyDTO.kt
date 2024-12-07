package com.clangengineer.exformmaker.service.dto

import java.io.Serializable
import java.util.Objects
import javax.validation.constraints.*

@SuppressWarnings("common-java:DuplicatedBlocks")
data class UserCompanyDTO(

    var id: Long? = null,

    var user: UserDTO? = null,

    var company: CompanyDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserCompanyDTO) return false
        val userCompanyDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, userCompanyDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}

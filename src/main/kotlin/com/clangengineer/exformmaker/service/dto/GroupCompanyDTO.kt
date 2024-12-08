package com.clangengineer.exformmaker.service.dto

import java.io.Serializable
import java.util.*

@SuppressWarnings("common-java:DuplicatedBlocks")
data class GroupCompanyDTO(

    var id: Long? = null,

    var group: GroupDTO? = null,

    var company: CompanyDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GroupCompanyDTO) return false
        val groupCompanyDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, groupCompanyDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}

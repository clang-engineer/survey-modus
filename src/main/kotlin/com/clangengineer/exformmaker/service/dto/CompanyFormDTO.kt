package com.clangengineer.exformmaker.service.dto

import java.io.Serializable
import java.util.*

@SuppressWarnings("common-java:DuplicatedBlocks")
data class CompanyFormDTO(
    var id: Long? = null,
    var company: CompanyDTO? = null,
    var form: FormDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CompanyFormDTO) return false
        val companyFormDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, companyFormDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}

package com.clangengineer.surveymodus.service.dto

import java.io.Serializable
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@SuppressWarnings("common-java:DuplicatedBlocks")
data class CategoryDTO(
    var id: Long? = null,

    @get: NotNull
    @get: Size(min = 5, max = 100)
    var title: String? = null,

    var description: String? = null,

    var activated: Boolean? = null,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CategoryDTO) return false
        val categoryDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, categoryDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}

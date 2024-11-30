package com.clangengineer.exformmaker.service.dto

import java.util.Objects
import javax.validation.constraints.*
import java.io.Serializable
import com.clangengineer.exformmaker.domain.enumeration.level


/**
 * A DTO for the [com.clangengineer.exformmaker.domain.Point] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class PointDTO(

    var id: Long? = null,

    @get: NotNull
    @get: Size(min = 20, max = 100)
    var title: String? = null,

    var description: String? = null,

    var activated: Boolean? = null,

    var type: level? = null
) : Serializable {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PointDTO) return false
        val pointDTO = other
        if (this.id == null){
            return false;
        }
        return Objects.equals(this.id, pointDTO.id);
    }

    override fun hashCode() =        Objects.hash(this.id)
}

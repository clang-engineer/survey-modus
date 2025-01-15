package com.clangengineer.surveymodus.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.LongFilter
import tech.jhipster.service.filter.StringFilter
import java.io.Serializable

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class FileCriteria(
    var id: LongFilter? = null,
    var filename: StringFilter? = null,
    var filepath: StringFilter? = null,
    var hashKey: StringFilter? = null,
) : Serializable, Criteria {

    constructor(other: FileCriteria) :
        this(
            other.id?.copy(),
            other.filename?.copy(),
            other.filepath?.copy(),
            other.hashKey?.copy(),
        )

    override fun copy() = FileCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

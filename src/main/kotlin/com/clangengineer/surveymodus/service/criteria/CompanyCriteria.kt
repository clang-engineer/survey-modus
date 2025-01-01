package com.clangengineer.surveymodus.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.BooleanFilter
import tech.jhipster.service.filter.LongFilter
import tech.jhipster.service.filter.StringFilter
import java.io.Serializable

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CompanyCriteria(
    var id: LongFilter? = null,
    var title: StringFilter? = null,
    var description: StringFilter? = null,
    var activated: BooleanFilter? = null,
    var userId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: CompanyCriteria) :
        this(
            other.id?.copy(),
            other.title?.copy(),
            other.description?.copy(),
            other.activated?.copy(),
            other.userId?.copy(),
            other.distinct
        )

    override fun copy() = CompanyCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

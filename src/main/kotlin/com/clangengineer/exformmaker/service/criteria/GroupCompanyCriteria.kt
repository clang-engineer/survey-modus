package com.clangengineer.exformmaker.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.LongFilter
import java.io.Serializable

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class GroupCompanyCriteria(
    var id: LongFilter? = null,
    var groupId: LongFilter? = null,
    var companyId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: GroupCompanyCriteria) :
        this(
            other.id?.copy(),
            other.groupId?.copy(),
            other.companyId?.copy(),
            other.distinct
        )

    override fun copy() = GroupCompanyCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

package com.clangengineer.exformmaker.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.LongFilter
import java.io.Serializable

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class GroupUserCriteria(
    var id: LongFilter? = null,
    var groupId: LongFilter? = null,
    var userId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: GroupUserCriteria) :
        this(
            other.id?.copy(),
            other.groupId?.copy(),
            other.userId?.copy(),
            other.distinct
        )

    override fun copy() = GroupUserCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

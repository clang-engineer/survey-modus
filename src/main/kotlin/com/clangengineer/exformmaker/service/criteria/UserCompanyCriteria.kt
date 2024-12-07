package com.clangengineer.exformmaker.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class UserCompanyCriteria(
    var id: LongFilter? = null,
    var userId: LongFilter? = null,
    var companyId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: UserCompanyCriteria) :
        this(
            other.id?.copy(),
            other.userId?.copy(),
            other.companyId?.copy(),
            other.distinct
        )

    override fun copy() = UserCompanyCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

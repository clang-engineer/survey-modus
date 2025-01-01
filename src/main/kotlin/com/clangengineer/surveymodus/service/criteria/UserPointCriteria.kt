package com.clangengineer.surveymodus.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

/**
 * Criteria class for the [com.clangengineer.surveymodus.domain.UserPoint] entity. This class is used in
 * [com.clangengineer.surveymodus.web.rest.UserPointResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/user-points?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class UserPointCriteria(
    var id: LongFilter? = null,
    var userId: LongFilter? = null,
    var pointId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: UserPointCriteria) :
        this(
            other.id?.copy(),
            other.userId?.copy(),
            other.pointId?.copy(),
            other.distinct
        )

    override fun copy() = UserPointCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

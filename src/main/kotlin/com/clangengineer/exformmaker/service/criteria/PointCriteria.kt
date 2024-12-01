package com.clangengineer.exformmaker.service.criteria

import com.clangengineer.exformmaker.domain.enumeration.level
import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.BooleanFilter
import tech.jhipster.service.filter.Filter
import tech.jhipster.service.filter.LongFilter
import tech.jhipster.service.filter.StringFilter
import java.io.Serializable

/**
 * Criteria class for the [com.clangengineer.exformmaker.domain.Point] entity. This class is used in
 * [com.clangengineer.exformmaker.web.rest.PointResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/points?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class PointCriteria(
    var id: LongFilter? = null,
    var title: StringFilter? = null,
    var description: StringFilter? = null,
    var activated: BooleanFilter? = null,
    var type: levelFilter? = null,
    var userId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: PointCriteria) :
        this(
            other.id?.copy(),
            other.title?.copy(),
            other.description?.copy(),
            other.activated?.copy(),
            other.type?.copy(),
            other.userId?.copy(),
            other.distinct
        )

    /**
     * Class for filtering level
     */
    class levelFilter : Filter<level> {
        constructor()

        constructor(filter: levelFilter) : super(filter)

        override fun copy() = levelFilter(this)
    }

    override fun copy() = PointCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

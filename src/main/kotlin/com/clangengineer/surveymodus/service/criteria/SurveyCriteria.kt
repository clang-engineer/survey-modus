package com.clangengineer.surveymodus.service.criteria

import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.LongFilter
import tech.jhipster.service.filter.StringFilter
import java.io.Serializable

data class SurveyCriteria(
    var id: StringFilter? = null,
    var companyId: LongFilter? = null,
    var formId: LongFilter? = null,
) : Serializable, Criteria {

    constructor(other: SurveyCriteria) :
        this(
            other.id?.copy(),
            other.companyId?.copy(),
            other.formId?.copy(),
        )

    override fun copy() = SurveyCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

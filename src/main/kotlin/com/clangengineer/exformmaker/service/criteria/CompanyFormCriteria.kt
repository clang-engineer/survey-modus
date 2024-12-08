package com.clangengineer.exformmaker.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.LongFilter
import java.io.Serializable

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CompanyFormCriteria(
    var id: LongFilter? = null,
    var companyId: LongFilter? = null,
    var formId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: CompanyFormCriteria) :
        this(
            other.id?.copy(),
            other.companyId?.copy(),
            other.formId?.copy(),
            other.distinct
        )

    override fun copy() = CompanyFormCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

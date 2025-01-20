package com.clangengineer.surveymodus.service.criteria

import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.LongFilter
import tech.jhipster.service.filter.StringFilter
import java.io.Serializable

data class MessageCriteria(
    var id: StringFilter? = null,
    var companyId: LongFilter? = null,
) : Serializable, Criteria {

    constructor(other: MessageCriteria) :
        this(
            other.id?.copy(),
            other.companyId?.copy(),
        )

    override fun copy() = MessageCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

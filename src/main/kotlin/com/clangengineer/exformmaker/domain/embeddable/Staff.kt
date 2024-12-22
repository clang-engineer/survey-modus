package com.clangengineer.exformmaker.domain.embeddable

import javax.persistence.Embeddable

@Embeddable
data class Staff(
    var activated: Boolean? = null,
    var name: String? = null,
    var email: String? = null,
    var phone: String? = null,
)

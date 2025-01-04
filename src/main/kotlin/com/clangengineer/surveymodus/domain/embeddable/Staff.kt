package com.clangengineer.surveymodus.domain.embeddable

import javax.persistence.Embeddable

@Embeddable
data class Staff(
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var activated: Boolean? = null,
    var langKey: String? = null,
    var phone: String? = null,
)

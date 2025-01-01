package com.clangengineer.surveymodus.domain.embeddable

import com.clangengineer.surveymodus.domain.enumeration.type
import javax.persistence.EnumType
import javax.persistence.Enumerated

data class FieldAttribute(
    @Enumerated(EnumType.STRING)
    var type: type? = null,
    var defaultValue: String? = null,
)

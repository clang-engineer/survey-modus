package com.clangengineer.exformmaker.domain.embeddable

import com.clangengineer.exformmaker.domain.enumeration.type
import javax.persistence.EnumType
import javax.persistence.Enumerated

data class FieldAttribute(
  @Enumerated(EnumType.STRING)
  var type: type? = null,
  var defaultValue: String? = null,
)

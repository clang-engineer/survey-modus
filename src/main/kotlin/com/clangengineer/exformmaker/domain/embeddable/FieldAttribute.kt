package com.clangengineer.exformmaker.domain.embeddable

import com.clangengineer.exformmaker.domain.enumeration.type

data class FieldAttribute(
  var type: type? = null,
  var defaultValue: String? = null,
  )
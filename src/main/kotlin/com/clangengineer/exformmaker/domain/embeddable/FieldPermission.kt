package com.clangengineer.exformmaker.domain.embeddable

data class FieldPermission(
  var exportable: Boolean? = null,
  var computable: Boolean? = null,
)
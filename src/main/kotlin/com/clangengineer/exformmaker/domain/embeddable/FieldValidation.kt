package com.clangengineer.exformmaker.domain.embeddable

data class FieldValidation(
  var required: Boolean? = null,
  var min: Int? = null,
  var max: Int? = null,
)

package com.clangengineer.surveymodus.service.dto

data class DocumentDTO(
    var form: FormDTO? = null,
    var row: Map<String, Any> = mapOf()
)

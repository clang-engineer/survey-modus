package com.clangengineer.surveymodus.service.dto

import com.clangengineer.surveymodus.config.DOCUMENT_FORM_ID

data class DocumentDTO(
    var form: FormDTO? = null,
    var row: Map<String, Any> = mapOf(),
) {

    fun toDocumentMap(): Map<String, Any> {
        val document = mutableMapOf<String, Any>()
        document[DOCUMENT_FORM_ID] = form?.id.toString()
        row.forEach { (key, value) -> document[key] = value }
        return document
    }
}

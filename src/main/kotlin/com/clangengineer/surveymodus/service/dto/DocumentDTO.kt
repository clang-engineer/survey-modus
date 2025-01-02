package com.clangengineer.surveymodus.service.dto

import org.bson.types.ObjectId

data class DocumentDTO(
    var _id: ObjectId? = null,
    var companyId: Long? = null,
    var formId: Long? = null,
    var fields: List<Map<String, Any>> = emptyList()
) {
    val id: String
        get() = _id?.toHexString() ?: ""
}
